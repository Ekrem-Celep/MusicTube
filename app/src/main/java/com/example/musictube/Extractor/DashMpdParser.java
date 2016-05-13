package com.example.musictube.Extractor;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class DashMpdParser {

    private DashMpdParser() {
    }

    static class DashMpdParsingException extends ParsingException {
        DashMpdParsingException(String message, Exception e) {
            super(message, e);
        }
    }

    public static List<AudioStream> getAudioStreams(String dashManifestUrl,
                                                    Downloader downloader)
            throws DashMpdParsingException {
        String dashDoc;
        try {
            dashDoc = downloader.download(dashManifestUrl);
        } catch(IOException ioe) {
            throw new DashMpdParsingException("Could not get dash mpd: " + dashManifestUrl, ioe);
        }
        Vector<AudioStream> audioStreams = new Vector<>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(dashDoc));
            String tagName = "";
            String currentMimeType = "";
            int currentBandwidth = -1;
            int currentSamplingRate = -1;
            boolean currentTagIsBaseUrl = false;
            for(int eventType = parser.getEventType();
                eventType != XmlPullParser.END_DOCUMENT;
                eventType = parser.next() ) {
                switch(eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if(tagName.equals("AdaptationSet")) {
                            currentMimeType = parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "mimeType");
                        } else if(tagName.equals("Representation") && currentMimeType.contains("audio")) {
                            currentBandwidth = Integer.parseInt(
                                    parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "bandwidth"));
                            currentSamplingRate = Integer.parseInt(
                                    parser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "audioSamplingRate"));
                        } else if(tagName.equals("BaseURL")) {
                            currentTagIsBaseUrl = true;
                        }
                        break;

                    case XmlPullParser.TEXT:
                        // actual stream tag
                        if(currentTagIsBaseUrl &&
                                (currentMimeType.contains("audio"))) {
                            int format = -1;
                            if(currentMimeType.equals(MediaFormat.WEBMA.mimeType)) {
                                format = MediaFormat.WEBMA.id;
                            } else if(currentMimeType.equals(MediaFormat.M4A.mimeType)) {
                                format = MediaFormat.M4A.id;
                            }
                            audioStreams.add(new AudioStream(parser.getText(),
                                    format, currentBandwidth, currentSamplingRate));
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(tagName.equals("AdaptationSet")) {
                            currentMimeType = "";
                        } else if(tagName.equals("BaseURL")) {
                            currentTagIsBaseUrl = false;
                        }
                        break;
                }
            }
        } catch(Exception e) {
            throw new DashMpdParsingException("Could not parse Dash mpd", e);
        }
        return audioStreams;
    }
}
