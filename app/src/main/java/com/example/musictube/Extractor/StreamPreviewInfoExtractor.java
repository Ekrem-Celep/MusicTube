package com.example.musictube.Extractor;

/**
 * Created by Ekrem on 2016-05-08.
 */
public interface StreamPreviewInfoExtractor {
    AbstractVideoInfo.StreamType getStreamType() throws ParsingException;
    String getWebPageUrl() throws ParsingException;
    String getTitle() throws ParsingException;
    int getDuration() throws ParsingException;
    String getUploader() throws ParsingException;
    String getUploadDate() throws ParsingException;
    long getViewCount() throws  ParsingException;
    String getThumbnailUrl() throws  ParsingException;
}
