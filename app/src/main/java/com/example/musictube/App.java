package com.example.musictube;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.musictube.Settings.SettingsActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import info.guardianproject.netcipher.NetCipher;
import info.guardianproject.netcipher.proxy.OrbotHelper;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class App extends Application {

    private static boolean useTor;

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialisera image loader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        /*if(prefs.getBoolean(getString(R.string.use_tor_key), false)) {
            OrbotHelper.requestStartTor(this);
            configureTor(true);
        } else {
            configureTor(false);
        }*/

        // DO NOT REMOVE THIS FUNCTION!!!
        // Otherwise downloadPathPreference has invalid value.
       // SettingsActivity.initSettings(this);
    }

    /**
     * Set the proxy settings based on whether Tor should be enabled or not.
     */
    /*public static void configureTor(boolean enabled) {
        useTor = enabled;
        if (useTor) {
            NetCipher.useTor();
        } else {
            NetCipher.setProxy(null);
        }
    }

    public static void checkStartTor(Context context) {
        if (useTor) {
            OrbotHelper.requestStartTor(context);
        }
    }

    public static boolean isUsingTor() {
        return useTor;
    }*/
}
