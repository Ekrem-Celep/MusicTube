package com.example.musictube.Extractor;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class ParsingException extends ExtractionException {
    public ParsingException(String message) {
        super(message);
    }
    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}