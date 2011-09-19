package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 12:10 PM
 */
public class JsonServiceClient {
    private HttpClient httpClient = new DefaultHttpClient();

    protected <T> T getResult(HttpUriRequest get, Class<T> aClass) throws IOException {
        return new Gson().fromJson(getReader(get), aClass);
    }
    protected <T> T getResult(HttpUriRequest get, Type type) throws IOException {
        return new Gson().fromJson(getReader(get), type);
    }

    private InputStreamReader getReader(HttpUriRequest get) throws IOException {
        HttpResponse response = httpClient.execute(get);
        String encoding = "UTF-8";
        if (response.getEntity().getContentEncoding() != null) {
            encoding = response.getEntity().getContentEncoding().getValue();
        }
        return new InputStreamReader(response.getEntity().getContent(),
                encoding);
    }



}
