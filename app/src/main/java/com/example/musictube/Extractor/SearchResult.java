package com.example.musictube.Extractor;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class SearchResult {
    public static SearchResult getSearchResult(SearchEngine engine, String query,
                                               int page, String languageCode, Downloader dl)
            throws ExtractionException, IOException {

        SearchResult result = engine.search(query, page, languageCode, dl).getSearchResult();
        if(result.resultList.isEmpty()) {
            if(result.suggestion.isEmpty()) {
                throw new ExtractionException("Empty result despite no error");
            } else {
                // This is used as a fallback. Do not relay on it !!!
                throw new SearchEngine.NothingFoundException(result.suggestion);
            }
        }
        return result;
    }

    public String suggestion = "";
    public final List<StreamPreviewInfo> resultList = new Vector<>();
    public List<Exception> errors = new Vector<>();
}
