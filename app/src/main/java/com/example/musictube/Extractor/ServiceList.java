package com.example.musictube.Extractor;

import com.example.musictube.Extractor.Services.YouTube.YoutubeService;

/**
 * Created by Ekrem on 2016-05-08.
 */
@SuppressWarnings("ALL")
public class ServiceList {

    private ServiceList() {
    }

    private static final String TAG = ServiceList.class.toString();
    private static final StreamingService[] services = {
            new YoutubeService(0)
    };
    public static StreamingService[] getServices() {
        return services;
    }
    public static StreamingService getService(int serviceId) throws ExtractionException {
        for(StreamingService s : services) {
            if(s.getServiceId() == serviceId) {
                return s;
            }
        }
        throw new ExtractionException("Service not known: " + Integer.toString(serviceId));
    }
    public static StreamingService getService(String serviceName) throws ExtractionException {
        return services[getIdOfService(serviceName)];
    }
    public static String getNameOfService(int id) {
        try {
            return getService(id).getServiceInfo().name;
        } catch (Exception e) {
            System.err.println("Service id not known");
            e.printStackTrace();
            return "";
        }
    }
    public static int getIdOfService(String serviceName) throws ExtractionException {
        for(int i = 0; i < services.length; i++) {
            if(services[i].getServiceInfo().name.equals(serviceName)) {
                return i;
            }
        }
        throw new ExtractionException("Error: Service " + serviceName + " not known.");
    }
}

