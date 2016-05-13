package com.example.musictube.Settings;

/**
 * Created by Ekrem on 2016-05-08.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.musictube.R;

import java.io.File;

/**
 * Helper for global settings
 */
public class MusicTubeSettings {

    private MusicTubeSettings() {
    }

    public static void initSettings(Context context) {
        PreferenceManager.setDefaultValues(context, R.xml.settings, false);
        getVideoDownloadFolder(context);
        getAudioDownloadFolder(context);
    }

    /* Redigera map för nerladdat vido */
    public static File getVideoDownloadFolder(Context context) {
        return getFolder(context, R.string.download_path_key, Environment.DIRECTORY_MOVIES);
    }

    /* Redigera map för nerladdat ljud/audio */
    public static File getAudioDownloadFolder(Context context) {
        return getFolder(context, R.string.download_path_audio_key, Environment.DIRECTORY_MUSIC);
    }

    /* Här skapar man sökvägen till ner laddade object och sen sätter ihop med
      * getVideoDownloadFolder metoden och getAudioDownloadFolder metoden.
       * Här döper vi även vad mappen ska heta som objecten laddas ner i ex. MusicTube*/
    private static File getFolder(Context context, int keyID, String defaultDirectoryName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final String key = context.getString(keyID);
        String downloadPath = prefs.getString(key, null);
        if ((downloadPath != null) && (!downloadPath.isEmpty())) return new File(downloadPath.trim());

        final File folder = getFolder(defaultDirectoryName);
        SharedPreferences.Editor spEditor = prefs.edit();
        spEditor.putString(key
                , new File(folder,"MusicTube").getAbsolutePath());
        spEditor.apply();
        return folder;
    }

    @NonNull
    private static File getFolder(String defaultDirectoryName) {
        return new File(Environment.getExternalStorageDirectory(),defaultDirectoryName);
    }
}
