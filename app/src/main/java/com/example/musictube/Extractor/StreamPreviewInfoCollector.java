package com.example.musictube.Extractor;

import com.example.musictube.Extractor.Services.YouTube.YoutubeStreamUrlIdHandler;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class StreamPreviewInfoCollector {
    private SearchResult result = new SearchResult();
    private StreamUrlIdHandler urlIdHandler = null;
    private int serviceId = -1;

    public StreamPreviewInfoCollector(StreamUrlIdHandler handler, int serviceId) {
        urlIdHandler = handler;
        this.serviceId = serviceId;
    }

    public void setSuggestion(String suggestion) {
        result.suggestion = suggestion;
    }

    public void addError(Exception e) {
        result.errors.add(e);
    }

    public SearchResult getSearchResult() {
        return result;
    }

    public void commit(StreamPreviewInfoExtractor extractor) throws ParsingException {
        try {
            StreamPreviewInfo resultItem = new StreamPreviewInfo();
            // importand information
            resultItem.service_id = serviceId;
            resultItem.webpage_url = extractor.getWebPageUrl();
            if (urlIdHandler == null) {
                throw new ParsingException("Error: UrlIdHandler not set");
            } else {
                resultItem.id = (new YoutubeStreamUrlIdHandler()).getVideoId(resultItem.webpage_url);
            }
            resultItem.title = extractor.getTitle();
            resultItem.stream_type = extractor.getStreamType();

            // optional iformation
            try {
                resultItem.duration = extractor.getDuration();
            } catch (Exception e) {
                addError(e);
            }
            try {
                resultItem.uploader = extractor.getUploader();
            } catch (Exception e) {
                addError(e);
            }
            try {
                resultItem.upload_date = extractor.getUploadDate();
            } catch (Exception e) {
                addError(e);
            }
            try {
                resultItem.view_count = extractor.getViewCount();
            } catch (Exception e) {
                addError(e);
            }
            try {
                resultItem.thumbnail_url = extractor.getThumbnailUrl();
            } catch (Exception e) {
                addError(e);
            }

            result.resultList.add(resultItem);
        } catch (Exception e) {
            addError(e);
        }

    }
}
