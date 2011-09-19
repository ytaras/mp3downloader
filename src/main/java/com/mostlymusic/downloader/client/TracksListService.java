package com.mostlymusic.downloader.client;


import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 3:59 PM
 */
public class TracksListService extends JsonServiceClient implements ITracksListService {
    private String serviceUrl;

    public TracksDto getTracks() throws IOException {
        HttpGet get = new HttpGet(serviceUrl);
        return getResult(get, TracksDto.class);
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
