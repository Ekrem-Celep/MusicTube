package com.example.musictube.Extractor;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class VideoStream {
    //url of the stream
    public String url = "";
    public int format = -1;
    public String resolution = "";

    public VideoStream(String url, int format, String res) {
        this.url = url; this.format = format; resolution = res;
    }

    // reveals wether two streams are the same, but have diferent urls
    public boolean equalStats(VideoStream cmp) {
        return format == cmp.format
                && resolution == cmp.resolution;
    }

    // revelas wether two streams are equal
    public boolean equals(VideoStream cmp) {
        return cmp != null && equalStats(cmp)
                && url == cmp.url;
    }
}
