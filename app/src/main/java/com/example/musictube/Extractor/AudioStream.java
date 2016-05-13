package com.example.musictube.Extractor;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class AudioStream {
    public String url = "";
    public int format = -1;
    public int bandwidth = -1;
    public int sampling_rate = -1;

    public AudioStream(String url, int format, int bandwidth, int samplingRate) {
        this.url = url; this.format = format;
        this.bandwidth = bandwidth; this.sampling_rate = samplingRate;
    }

    // reveals wether two streams are the same, but have diferent urls
    public boolean equalStats(AudioStream cmp) {
        return format == cmp.format
                && bandwidth == cmp.bandwidth
                && sampling_rate == cmp.sampling_rate;
    }

    // revelas wether two streams are equal
    public boolean equals(AudioStream cmp) {
        return cmp != null && equalStats(cmp)
                && url == cmp.url;
    }
}