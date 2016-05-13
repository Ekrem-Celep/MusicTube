package com.example.musictube.Settings;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;

import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.musictube.App;
import com.example.musictube.R;

import java.util.List;

import info.guardianproject.netcipher.proxy.OrbotHelper;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

    private static final int REQUEST_INSTALL_ORBOT = 0x1234;
    private AppCompatDelegate mDelegate = null;

    @Override
    protected void onCreate(Bundle savedInstanceBundle) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceBundle);
        super.onCreate(savedInstanceBundle);

        getLayoutInflater().inflate(R.layout.activity_settings,
                (ViewGroup)findViewById(android.R.id.content));

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment())
                .commit();

    }

    public static class SettingsFragment extends PreferenceFragment{
        SharedPreferences.OnSharedPreferenceChangeListener prefListener;

        // Hämtar värde-nycklarna
        String DEFAULT_RESOLUTION_PREFERENCE;
        String DEFAULT_AUDIO_FORMAT_PREFERENCE;
        String SEARCH_LANGUAGE_PREFERENCE;
        String DOWNLOAD_PATH_PREFERENCE;
        String DOWNLOAD_PATH_AUDIO_PREFERENCE;
        String USE_TOR_KEY;

        private ListPreference defaultResolutionPreference;
        private ListPreference defaultAudioFormatPreference;
        private ListPreference searchLanguagePreference;
        private EditTextPreference downloadPathPreference;
        private EditTextPreference downloadPathAudioPreference;
        private SwitchPreference useTorCheckBox;
        private SharedPreferences defaultPreferences;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            final Activity activity = getActivity();

            defaultPreferences = PreferenceManager.getDefaultSharedPreferences(activity);

            // Hämtar värde-nycklar för förutdefinerade inställningar
            DEFAULT_RESOLUTION_PREFERENCE =getString(R.string.default_resolution_key);
            DEFAULT_AUDIO_FORMAT_PREFERENCE =getString(R.string.default_audio_format_key);
            SEARCH_LANGUAGE_PREFERENCE =getString(R.string.search_language_key);
            DOWNLOAD_PATH_PREFERENCE = getString(R.string.download_path_key);
            DOWNLOAD_PATH_AUDIO_PREFERENCE = getString(R.string.download_path_audio_key);


            // Hämtar insällningar för objekt
            defaultResolutionPreference =
                    (ListPreference) findPreference(DEFAULT_RESOLUTION_PREFERENCE);
            defaultAudioFormatPreference =
                    (ListPreference) findPreference(DEFAULT_AUDIO_FORMAT_PREFERENCE);
            searchLanguagePreference =
                    (ListPreference) findPreference(SEARCH_LANGUAGE_PREFERENCE);
            downloadPathPreference =
                    (EditTextPreference) findPreference(DOWNLOAD_PATH_PREFERENCE);
            downloadPathAudioPreference =
                    (EditTextPreference) findPreference(DOWNLOAD_PATH_AUDIO_PREFERENCE);
            useTorCheckBox = (SwitchPreference) findPreference(USE_TOR_KEY);

            prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    Activity a = getActivity();
                    if(a != null) {
                        updateSummary();
                    }
                }
            };
            defaultPreferences.registerOnSharedPreferenceChangeListener(prefListener);

            updateSummary();
        }

        // Detta används för att visa status för någon preferens i beskrivningen.
        // Man kan säga att fördefinierade inställningar
        private void updateSummary() {
            defaultResolutionPreference.setSummary(
                    defaultPreferences.getString(DEFAULT_RESOLUTION_PREFERENCE,
                            getString(R.string.default_resolution_value)));
            defaultAudioFormatPreference.setSummary(
                    defaultPreferences.getString(DEFAULT_AUDIO_FORMAT_PREFERENCE,
                            getString(R.string.default_audio_format_value)));
            searchLanguagePreference.setSummary(
                    defaultPreferences.getString(SEARCH_LANGUAGE_PREFERENCE,
                            getString(R.string.default_language_value)));
            downloadPathPreference.setSummary(
                    defaultPreferences.getString(DOWNLOAD_PATH_PREFERENCE,
                            getString(R.string.download_path_summary)));
            downloadPathAudioPreference.setSummary(
                    defaultPreferences.getString(DOWNLOAD_PATH_AUDIO_PREFERENCE,
                            getString(R.string.download_path_audio_summary)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    private ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @NonNull
    @Override
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            finish();
        }
        return true;
    }

    public static void initSettings(Context context) {
        MusicTubeSettings.initSettings(context);
    }
}
