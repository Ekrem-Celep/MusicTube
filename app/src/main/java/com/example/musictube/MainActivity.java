package com.example.musictube;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.musictube.Activities.ErrorActivity;
import com.example.musictube.Activities.NavHome;
import com.example.musictube.Activities.VideoItemDetailActivity;
import com.example.musictube.AppIntro.AppIntro;
import com.example.musictube.Download.Downloader;
import com.example.musictube.Extractor.ExtractionException;
import com.example.musictube.Extractor.SearchEngine;
import com.example.musictube.Extractor.ServiceList;
import com.example.musictube.Extractor.StreamingService;
import com.example.musictube.Fonts.AppFont;
import com.example.musictube.Fragments.VideoItemDetailFragment;
import com.example.musictube.Fragments.VideoItemListFragment;
import com.example.musictube.Settings.SettingsActivity;
import com.example.musictube.Video.VideoListAdapter;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements VideoItemListFragment.Callbacks {
    private static final String TAG = VideoItemListFragment.class.toString();

    public static final String VIDEO_INFO_ITEMS = "video_info_items";

    //savedInstanceBundle argument
    private static final String QUERY = "query";
    private static final String STREAMING_SERVICE = "streaming_service";

    private static final int SEARCH_MODE = 0;
    private static final int PRESENT_VIDEOS_MODE = 1;

    private int mode = SEARCH_MODE;
    private int currentStreamingServiceId = -1;
    private String searchQuery = "";

    private VideoItemListFragment listFragment;
    private VideoItemDetailFragment videoFragment = null;
    private Menu menu = null;

    private SuggestionListAdapter suggestionListAdapter;
    private SuggestionSearchRunnable suggestionSearchRunnable;
    private Thread searchThread;

    private DrawerLayout mNavigationDrawer;
    private NavigationView mNavigationView;
    private Toolbar mToolbar;

    private Context context;

    private class SearchVideoQueryListener implements SearchView.OnQueryTextListener {

        /* När man klickar på sök iconen ska kommer tagnetbordet upp och man börjar skirva. Efter
         * när man har tryckt på sök iconen i tagnetbordet så ska tagnetbordet försvinna */
        @Override
        public boolean onQueryTextSubmit(String query) {
            try {
                searchQuery = query;
                listFragment.search(query);

                // gömma virtuella tangentbordet
                InputMethodManager inputManager =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    inputManager.hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (NullPointerException e) {
                    Log.e(TAG, "Could not get widget with focus");
                    Toast.makeText(MainActivity.this, "Could not get widget with focus",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                // clear focus
                // Att inte öppna upp tangentbordet igen

                // Se: http://stackoverflow.com/questions/17874951/searchview-onquerytextsubmit-runs-twice-while-i-pressed-once
                getCurrentFocus().clearFocus();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        /* När man går tillbaka till sök orden så ska man kunna redigera det, så
         * den kollar om den inte är tom eller tom */
        @Override
        public boolean onQueryTextChange(String newText) {
            if (!newText.isEmpty()) {
                searchSuggestions(newText);
            }
            return true;
        }

    }

    private class SearchSuggestionListener implements SearchView.OnSuggestionListener {

        private SearchView searchView;

        private SearchSuggestionListener(SearchView searchView) {
            this.searchView = searchView;
        }


        /* När man väljer förslag från SuggestionListAdapter */
        @Override
        public boolean onSuggestionSelect(int position) {
            String suggestion = suggestionListAdapter.getSuggestion(position);
            searchView.setQuery(suggestion, true);
            return false;
        }

        /* När man väljer förslag från SuggestionListAdapter */
        @Override
        public boolean onSuggestionClick(int position) {
            String suggestion = suggestionListAdapter.getSuggestion(position);
            searchView.setQuery(suggestion, true);
            return false;
        }
    }

    /* Ser till att när man väljer från SuggestionListAdapter får action/körs
     * gör sökning */
    private class SuggestionResultRunnable implements Runnable {

        private ArrayList<String> suggestions;

        private SuggestionResultRunnable(ArrayList<String> suggestions) {
            this.suggestions = suggestions;
        }

        @Override
        public void run() {
            suggestionListAdapter.updateAdapter(suggestions);
        }
    }

    private class SuggestionSearchRunnable implements Runnable {
        private final int serviceId;
        private final String query;
        final Handler h = new Handler();
        private Context context;

        private SuggestionSearchRunnable(int serviceId, String query) {
            this.serviceId = serviceId;
            this.query = query;
            context = MainActivity.this;
        }

        /* ska hjälpa till med att söka på ett specefik språk när man söker på YouTube. Om det
          * inte hittar på det specefika språket så ska man kunna skicka ett fel rappot om detta
           * Det här år nåt som jag jobbar på*/
        @Override
        public void run() {
            try {
                SearchEngine engine =
                        ServiceList.getService(serviceId).getSearchEngineInstance(new Downloader());
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                String searchLanguageKey = context.getString(R.string.search_language_key);
                String searchLanguage = sp.getString(searchLanguageKey,
                        getString(R.string.default_language_value));
                ArrayList<String> suggestions = engine.suggestionList(query, searchLanguage, new Downloader());
                h.post(new SuggestionResultRunnable(suggestions));
            } catch (ExtractionException e) {
                ErrorActivity.reportError(h, MainActivity.this, e, null, findViewById(R.id.videoitem_list),
                        ErrorActivity.ErrorInfo.make(ErrorActivity.SEARCHED,
                                ServiceList.getNameOfService(serviceId), query, R.string.parsing_error));
                e.printStackTrace();
            } catch (IOException e) {
                // postNewErrorToast(h, R.string.network_error);
                e.printStackTrace();
            } catch (Exception e) {
                ErrorActivity.reportError(h, MainActivity.this, e, null, findViewById(R.id.videoitem_list),
                        ErrorActivity.ErrorInfo.make(ErrorActivity.SEARCHED,
                                ServiceList.getNameOfService(serviceId), query, R.string.general_error));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this; // context/sammanhang till MaterialStyleDialog

        /*
           Sätter fonten för hela appen
         */
        AppFont.replaceDefaultFont(this, "DEFAULT", "Fonts/OpenSans-Regular.ttf");

        /* Kör AppIntro activity en gång när appen installeras för första gången */

        //  Deklarerar en ny tråd för att göra en preferens kontroll
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Sätter igång SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Skapar en ny boolean och kollar om det är första gången när appen startar
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  Om activity inte har startat tidigare...
                if (isFirstStart) {

                    //  Kör AppIntro activity
                    Intent i = new Intent(MainActivity.this, AppIntro.class);
                    startActivity(i);

                    //  Skapar en ny preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Här sätter vi preference till false aå att activity:n inte körs igen
                    e.putBoolean("firstStart", true);

                    //  Tillämpa ändringarna
                    e.apply();
                }
            }
        });

        // Starta tråden(thred)
        t.start();

        mNavigationDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mNavigationDrawer, mToolbar,
                R.string.app_name, R.string.app_name);

        mNavigationDrawer.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();

        handleNavigationView();

        StreamingService streamingService = null;

        try {
            //------ todo: remove this line when multiservice support is implemented ------
            currentStreamingServiceId = ServiceList.getIdOfService("Youtube");
            streamingService = ServiceList.getService(currentStreamingServiceId);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorActivity.reportError(MainActivity.this, e, null, findViewById(R.id.videoitem_list),
                    ErrorActivity.ErrorInfo.make(ErrorActivity.SEARCHED,
                            ServiceList.getNameOfService(currentStreamingServiceId), "", R.string.general_error));
        }

        listFragment = (VideoItemListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.videoitem_list);
        listFragment.setStreamingService(streamingService);

        if (savedInstanceState != null
                && mode != PRESENT_VIDEOS_MODE) {
            searchQuery = savedInstanceState.getString(QUERY);
            currentStreamingServiceId = savedInstanceState.getInt(STREAMING_SERVICE);
            if (!searchQuery.isEmpty()) {
                listFragment.search(searchQuery);
            }
        }

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

    }

    /* Hämtar id:en i navigation_menu för drawerlayout och sätter klickningar med switch case  */
    public void handleNavigationView() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                handleNavDrawer();
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        Intent home = new Intent(getApplicationContext(), NavHome.class);
                        startActivity(home);
                        break;
                    case R.id.nav_frag_one:

                        break;
                    case R.id.nav_frag_two:

                        break;
                    case R.id.nav_frag_three:

                        break;
                    case R.id.nav_settings:
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_about:
                        MaterialStyledDialog dialog = new MaterialStyledDialog(context)
                                .setTitle("Awesome!")
                                .setDescription("Nice JOBB.")
                                .build();

                        dialog.show();
                    default:
                        break;
                }
                return false;
            }
        });
    }

    /*
     * Callback -metoden från { @link VideoItemListFragment.Callbacks }
     * Indikerar att objektet med den givna ID valdes och laddar upp resultaten
     * med VideoItemDetailFragment
     */
    @Override
    public void onItemSelected(String id) {
        VideoListAdapter listAdapter = (VideoListAdapter) ((VideoItemListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.videoitem_list))
                .getListAdapter();
        String webpageUrl = listAdapter.getVideoList().get((int) Long.parseLong(id)).webpage_url;

        // Börja detalj aktivitet för videon för det markerade objektet ID
        Intent detailIntent = new Intent(this, VideoItemDetailActivity.class);
        //detailIntent.putExtra(VideoItemDetailFragment.ARG_ITEM_ID, id);
        detailIntent.putExtra(VideoItemDetailFragment.VIDEO_URL, webpageUrl);
        detailIntent.putExtra(VideoItemDetailFragment.STREAMING_SERVICE, currentStreamingServiceId);
        startActivity(detailIntent);


    }

    /*  läser in XML-fil som beskriver en layout ( eller GUI element) och skapar själva objektet
        som motsvarar den , och på så sätt göra föremålet synliga */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();

        // Om layouten för VideoItemDetailActivity har inget att visa då sätter vi den med menu
        // layouten. Sen tar vi id:en för sök knappen och gör den till en objekt som vi kan klicka
        // på(serachItem)
        if (mode != PRESENT_VIDEOS_MODE &&
                findViewById(R.id.videoitem_detail_container) == null) {
            inflater.inflate(R.menu.menu_main, menu);

            MenuItem searchItem = menu.findItem(R.id.action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setFocusable(false);
            searchView.setQueryHint("Search");

            // searchView kopplas till SuggestionListAdapter
            searchView.setOnQueryTextListener(
                    new SearchVideoQueryListener());
            suggestionListAdapter = new SuggestionListAdapter(this);
            searchView.setSuggestionsAdapter(suggestionListAdapter);
            searchView.setOnSuggestionListener(new SearchSuggestionListener(searchView));
            if (!searchQuery.isEmpty()) {
                searchView.setQuery(searchQuery, false);
                searchView.setIconifiedByDefault(false);
            }
        } else if (videoFragment != null) {
            videoFragment.onCreateOptionsMenu(menu, inflater);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Tar hand om klickningar från actionbar, som sätts i menu_main. Samma sak här man ropar
        *  på id:en från menun */
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
          /*  case R.id.action_report_error: {
                ErrorActivity.reportError(MainActivity.this, new Vector<Exception>(),
                        null, null,
                        ErrorActivity.ErrorInfo.make(ErrorActivity.USER_REPORT,
                                ServiceList.getNameOfService(currentStreamingServiceId),
                                "user_report", R.string.user_report));
                return true;
            }*/
            default:
                return videoFragment.onOptionsItemSelected(item) ||
                        super.onOptionsItemSelected(item);
        }
    }

    /* onSaveInstanceState ser till så att activity:n sparar tillståndsinformation med en samling
       av nyckel-värdepar. Här sparars tillståndsinformation hierarkiskt,
       såsom texten i en Edittext widget eller rullningspositionen för en Listview
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
        */
        outState.putString(QUERY, searchQuery);
        outState.putInt(STREAMING_SERVICE, currentStreamingServiceId);
    }

    // Söker på förslag
    private void searchSuggestions(String query) {
        suggestionSearchRunnable =
                new SuggestionSearchRunnable(currentStreamingServiceId, query);
        searchThread = new Thread(suggestionSearchRunnable);
        searchThread.start();

    }

   /* private void postNewErrorToast(Handler h, final int stringResource) {
        h.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, getString(stringResource),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    /* Tar hand så att drawerlayot stängs automatisk efter man har gjort ett val/klickning*/
    public void handleNavDrawer() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
    }

}
