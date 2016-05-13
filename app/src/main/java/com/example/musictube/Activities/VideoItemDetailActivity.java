package com.example.musictube.Activities;

import android.content.Intent;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.musictube.App;
import com.example.musictube.Extractor.ServiceList;
import com.example.musictube.Extractor.StreamingService;
import com.example.musictube.Fragments.VideoItemDetailFragment;
import com.example.musictube.MainActivity;
import com.example.musictube.R;

public class VideoItemDetailActivity extends AppCompatActivity {

    private static final String TAG = VideoItemDetailActivity.class.toString();

    private VideoItemDetailFragment fragment;

    private String videoUrl;
    private int currentStreamingService = -1;

    private Toolbar mToolbar;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_item_detail);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        setVolumeControlStream(AudioManager.STREAM_MUSIC); // kontollera volumen för appen
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(Exception e) {
            Log.d(TAG, "Could not get SupportActionBar");
            e.printStackTrace();
        }

        Bundle arguments = new Bundle();
        if (savedInstanceState == null) {
            // Detta innebär videon kallades genom en annan app
            if (getIntent().getData() != null) {
                videoUrl = getIntent().getData().toString();
                StreamingService[] serviceList = ServiceList.getServices();
                //StreamExtractor videoExtractor = null;
                for (int i = 0; i < serviceList.length; i++) {
                    if (serviceList[i].getUrlIdHandlerInstance().acceptUrl(videoUrl)) {
                        arguments.putInt(VideoItemDetailFragment.STREAMING_SERVICE, i);
                        currentStreamingService = i;
                        //videoExtractor = ServiceList.getService(i).getExtractorInstance();
                        break;
                    }
                }
                if(currentStreamingService == -1) {
                    Toast.makeText(this, R.string.url_not_supported_toast, Toast.LENGTH_LONG)
                            .show();
                }
                //arguments.putString(VideoItemDetailFragment.VIDEO_URL,
                //        videoExtractor.getVideoUrl(videoExtractor.getVideoId(videoUrl)));//cleans URL
                arguments.putString(VideoItemDetailFragment.VIDEO_URL, videoUrl);

                arguments.putBoolean(VideoItemDetailFragment.AUTO_PLAY,
                        PreferenceManager.getDefaultSharedPreferences(this)
                                .getBoolean(getString(R.string.autoplay_through_intent_key), false));
            } else {
                videoUrl = getIntent().getStringExtra(VideoItemDetailFragment.VIDEO_URL);
                currentStreamingService = getIntent().getIntExtra(VideoItemDetailFragment.STREAMING_SERVICE, -1);
                arguments.putString(VideoItemDetailFragment.VIDEO_URL, videoUrl);
                arguments.putInt(VideoItemDetailFragment.STREAMING_SERVICE, currentStreamingService);
                arguments.putBoolean(VideoItemDetailFragment.AUTO_PLAY, false);
            }

        } else {
            videoUrl = savedInstanceState.getString(VideoItemDetailFragment.VIDEO_URL);
            currentStreamingService = savedInstanceState.getInt(VideoItemDetailFragment.STREAMING_SERVICE);
            arguments = savedInstanceState;
        }

        // Skapa detalj fragment och lägga till den activity:n
        // med hjälp av fragment transaktion
        fragment = new VideoItemDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.videoitem_detail_container, fragment)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(VideoItemDetailFragment.VIDEO_URL, videoUrl);
        outState.putInt(VideoItemDetailFragment.STREAMING_SERVICE, currentStreamingService);
        outState.putBoolean(VideoItemDetailFragment.AUTO_PLAY, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Använder NavUtils att tillåta användare
            // För att navigera upp en nivå i programstrukturen

            // http://developer.android.com/design/patterns/navigation.html#up-vs-back

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            NavUtils.navigateUpTo(this, intent);
            return true;
        } else {
            return fragment.onOptionsItemSelected(item) ||
                    super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        fragment.onCreateOptionsMenu(menu, getMenuInflater());
        return true;
    }
}

