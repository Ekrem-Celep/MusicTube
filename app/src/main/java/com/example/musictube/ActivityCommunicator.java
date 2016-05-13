package com.example.musictube;

/**
 * Created by Ekrem on 2016-05-08.
 */

import android.graphics.Bitmap;

import com.example.musictube.Activities.ErrorActivity;

import java.util.List;

/*
 * Singleton :
 * Används för att skicka data mellan vissa Aktivitet / tjänster inom samma process **/
public class ActivityCommunicator {

    private static ActivityCommunicator activityCommunicator = null;

    public static ActivityCommunicator getCommunicator() {
        if(activityCommunicator == null) {
            activityCommunicator = new ActivityCommunicator();
        }
        return activityCommunicator;
    }

    // Thumbnail skicka från VideoItemDetailFragment till BackgroundPlayer
    public volatile Bitmap backgroundPlayerThumbnail;

    // Skickas från någon aktivitet till ErrorActivity .
    public volatile List<Exception> errorList;
    public volatile Class returnActivity;
    public volatile ErrorActivity.ErrorInfo errorInfo;
}
