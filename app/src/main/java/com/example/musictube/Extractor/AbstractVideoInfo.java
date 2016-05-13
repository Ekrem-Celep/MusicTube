package com.example.musictube.Extractor;

/**
 * Created by Ekrem on 2016-05-08.
 */

import android.graphics.Bitmap;

/* Gemensamma egenskaper mellan StreamInfo och StreamPreviewInfo.*/
public abstract class AbstractVideoInfo {
    public static enum StreamType {
        NONE,   // platshållare för att kontrollera om ström typ kontrollerades eller inte
        VIDEO_STREAM,
        AUDIO_STREAM,
        LIVE_STREAM,
        AUDIO_LIVE_STREAM,
        FILE
    }

    public StreamType stream_type;
    public int service_id = -1;
    public String id = "";
    public String title = "";
    public String uploader = "";
    public String thumbnail_url = "";
    public Bitmap thumbnail = null;
    public String webpage_url = "";
    public String upload_date = "";
    public long view_count = -1;
}
