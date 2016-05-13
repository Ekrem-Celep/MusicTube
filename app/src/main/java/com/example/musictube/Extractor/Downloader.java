package com.example.musictube.Extractor;

import java.io.IOException;

/**
 * Created by Ekrem on 2016-05-08.
 */
public interface Downloader {

    /**Download the text file at the supplied URL as in download(String),
     * but set the HTTP header field "Accept-Language" to the supplied string.
     * @param siteUrl the URL of the text file to return the contents of
     * @param language the language (usually a 2-character code) to set as the preferred language
     * @return the contents of the specified text file
     * @throws IOException */
    String download(String siteUrl, String language) throws IOException;

    /**Download (via HTTP) the text file located at the supplied URL, and return its contents.
     * Primarily intended for downloading web pages.
     * @param siteUrl the URL of the text file to download
     * @return the contents of the specified text file
     * @throws IOException*/
    String download(String siteUrl) throws IOException;
}
