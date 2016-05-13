package com.example.musictube.Extractor.Services.YouTube;

import com.example.musictube.Extractor.Downloader;
import com.example.musictube.Extractor.ExtractionException;
import com.example.musictube.Extractor.SearchEngine;
import com.example.musictube.Extractor.StreamExtractor;
import com.example.musictube.Extractor.StreamUrlIdHandler;
import com.example.musictube.Extractor.StreamingService;

import java.io.IOException;

/**
 * Created by Ekrem on 2016-05-08.
 */
public class YoutubeService extends StreamingService {

    public YoutubeService(int id) {
        super(id);
    }

    @Override
    public StreamingService.ServiceInfo getServiceInfo() {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.name = "Youtube";
        return serviceInfo;
    }
    @Override
    public StreamExtractor getExtractorInstance(String url, Downloader downloader)
            throws ExtractionException, IOException {
        StreamUrlIdHandler urlIdHandler = new YoutubeStreamUrlIdHandler();
        if(urlIdHandler.acceptUrl(url)) {
            return new YoutubeStreamExtractor(url, downloader, getServiceId());
        }
        else {
            throw new IllegalArgumentException("supplied String is not a valid Youtube URL");
        }
    }
    @Override
    public SearchEngine getSearchEngineInstance(Downloader downloader) {
        return new YoutubeSearchEngine(getUrlIdHandlerInstance(), getServiceId());
    }

    @Override
    public StreamUrlIdHandler getUrlIdHandlerInstance() {
        return new YoutubeStreamUrlIdHandler();
    }
}
