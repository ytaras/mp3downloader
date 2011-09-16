package com.mostlymusic.downloader.client;


import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 3:59 PM
 */
public class TracksListService implements ITracksListService {
    private String serviceUrl;
    private HttpClient httpClient = new DefaultHttpClient();

    public TracksDto getTracks() throws IOException {
        HttpGet get = new HttpGet(serviceUrl);
        HttpResponse execute = httpClient.execute(get);
        // TODO Deal with encodings
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        execute.getEntity().writeTo(byteArrayOutputStream);
        String data = byteArrayOutputStream.toString();

        return new Gson().fromJson(data, TracksDto.class);
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
