package com.example.musictube.Extractor;

/**
 * Created by Ekrem on 2016-05-08.
 */

import java.util.List;

/**Scrapes information from a video streaming service (eg, YouTube).*/


@SuppressWarnings("ALL")
public abstract class StreamExtractor {

    private int serviceId;

    public class ExctractorInitException extends ExtractionException {
        public ExctractorInitException(String message) {
            super(message);
        }
        public ExctractorInitException(Throwable cause) {
            super(cause);
        }
        public ExctractorInitException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public class ContentNotAvailableException extends ParsingException {
        public ContentNotAvailableException(String message) {
            super(message);
        }
        public ContentNotAvailableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public StreamExtractor(String url, Downloader dl, int serviceId) {
        this.serviceId = serviceId;
    }

    public abstract int getTimeStamp() throws ParsingException;
    public abstract String getTitle() throws ParsingException;
    public abstract String getDescription() throws ParsingException;
    public abstract String getUploader() throws ParsingException;
    public abstract int getLength() throws ParsingException;
    public abstract long getViewCount() throws ParsingException;
    public abstract String getUploadDate() throws ParsingException;
    public abstract String getThumbnailUrl() throws ParsingException;
    public abstract String getUploaderThumbnailUrl() throws ParsingException;
    public abstract List<AudioStream> getAudioStreams() throws ParsingException;
    public abstract List<VideoStream> getVideoStreams() throws ParsingException;
    public abstract List<VideoStream> getVideoOnlyStreams() throws ParsingException;
    public abstract String getDashMpdUrl() throws ParsingException;
    public abstract int getAgeLimit() throws ParsingException;
    public abstract String getAverageRating() throws ParsingException;
    public abstract int getLikeCount() throws ParsingException;
    public abstract int getDislikeCount() throws ParsingException;
    public abstract StreamPreviewInfo getNextVideo() throws ParsingException;
    public abstract List<StreamPreviewInfo> getRelatedVideos() throws ParsingException;
    public abstract StreamUrlIdHandler getUrlIdConverter();
    public abstract String getPageUrl();
    public abstract StreamInfo.StreamType getStreamType() throws ParsingException;
    public int getServiceId() {
        return serviceId;
    }
}
