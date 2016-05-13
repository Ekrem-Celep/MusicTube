package com.example.musictube.Extractor.Services.YouTube;

import com.example.musictube.Extractor.Parser;
import com.example.musictube.Extractor.ParsingException;
import com.example.musictube.Extractor.StreamUrlIdHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class YoutubeStreamUrlIdHandler implements StreamUrlIdHandler {
    @SuppressWarnings("WeakerAccess")
    @Override
    public String getVideoUrl(String videoId) {
        return "https://www.youtube.com/watch?v=" + videoId;
    }

    @SuppressWarnings("WeakerAccess")
    @Override
    public String getVideoId(String url) throws ParsingException {
        String id;

        if(url.contains("youtube")) {
            if(url.contains("attribution_link")) {
                try {
                    String escapedQuery = Parser.matchGroup1("u=(.[^&|$]*)", url);
                    String query = URLDecoder.decode(escapedQuery, "UTF-8");
                    id = Parser.matchGroup1("v=([\\-a-zA-Z0-9_]{11})", query);
                } catch(UnsupportedEncodingException uee) {
                    throw new ParsingException("Could not parse attribution_link", uee);
                }
            } else {
                id = Parser.matchGroup1("[?&]v=([\\-a-zA-Z0-9_]{11})", url);
            }
        }
        else if(url.contains("youtu.be")) {
            if(url.contains("v=")) {
                id = Parser.matchGroup1("v=([\\-a-zA-Z0-9_]{11})", url);
            } else {
                id = Parser.matchGroup1("youtu\\.be/([a-zA-Z0-9_-]{11})", url);
            }
        }
        else {
            throw new ParsingException("Error no suitable url: " + url);
        }


        if(!id.isEmpty()){
            return id;
        } else {
            throw new ParsingException("Error could not parse url: " + url);
        }
    }

    public String cleanUrl(String complexUrl) throws ParsingException {
        return getVideoUrl(getVideoId(complexUrl));
    }

    @Override
    public boolean acceptUrl(String videoUrl) {
        return videoUrl.contains("youtube") ||
                videoUrl.contains("youtu.be");
    }
}
