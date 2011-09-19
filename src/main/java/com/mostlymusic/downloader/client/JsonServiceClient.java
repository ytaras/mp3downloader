package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 12:10 PM
 */
public class JsonServiceClient {
    private HttpClient httpClient = new DefaultHttpClient();

    protected <T> T getResult(HttpUriRequest get, Class<T> classOfT) throws IOException {
        HttpResponse execute = httpClient.execute(get);
        // TODO Deal with encodings
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        execute.getEntity().writeTo(byteArrayOutputStream);
        String data = byteArrayOutputStream.toString();
        return new Gson().fromJson(data, classOfT);
    }
}
