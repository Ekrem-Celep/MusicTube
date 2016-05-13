package com.example.musictube.Extractor;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class ExtractionException extends Exception {
    public ExtractionException(String message) {
        super(message);
    }

    public ExtractionException(Throwable cause) {
        super(cause);
    }

    public ExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
