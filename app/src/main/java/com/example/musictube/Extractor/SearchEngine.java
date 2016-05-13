package com.example.musictube.Extractor;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ekrem on 2016-05-08.
 */
@SuppressWarnings("ALL")
public abstract class SearchEngine {
    public static class NothingFoundException extends ExtractionException {
        public NothingFoundException(String message) {
            super(message);
        }
    }

    private StreamPreviewInfoCollector collector;

    public SearchEngine(StreamUrlIdHandler urlIdHandler, int serviceId) {
        collector = new StreamPreviewInfoCollector(urlIdHandler, serviceId);
    }

    public StreamPreviewInfoCollector getStreamPreviewInfoCollector() {
        return collector;
    }

    public abstract ArrayList<String> suggestionList(
            String query,String contentCountry, Downloader dl)
            throws ExtractionException, IOException;

    //Result search(String query, int page);
    public abstract StreamPreviewInfoCollector search(
            String query, int page, String contentCountry, Downloader dl)
            throws ExtractionException, IOException;
}
