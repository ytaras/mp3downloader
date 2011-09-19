package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
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
    // TODO Inject
    private Gson gson = new Gson();

    protected <T> T getResult(HttpUriRequest get, Class<T> aClass) throws IOException {
        return gson.fromJson(getReader(get), aClass);
    }

    protected <T> T getResult(HttpUriRequest get, Type type) throws IOException {
        return gson.fromJson(getReader(get), type);
    }

    private InputStreamReader getReader(HttpUriRequest get) throws IOException {
        HttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();
        String encoding = "UTF-8";
        if (entity.getContentEncoding() != null) {
            encoding = entity.getContentEncoding().getValue();
        }

        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            entity.writeTo(stream);
            throw new HttpResponseException(response.getStatusLine().getStatusCode(), stream.toString(encoding));
        }

        return new InputStreamReader(entity.getContent(), encoding);
    }


}
