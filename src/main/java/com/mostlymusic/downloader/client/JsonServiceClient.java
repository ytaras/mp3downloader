package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import com.google.inject.Inject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 12:10 PM
 */
public class JsonServiceClient {

    private static final String DEFAULT_ENCODING = "UTF-8";
    private DefaultHttpClient httpClient;
    private Gson gson;

    protected <T> T getResult(HttpUriRequest get, Class<T> aClass) throws IOException {
        return gson.fromJson(getReader(get), aClass);
    }

    protected <T> T getResult(HttpUriRequest get, Type type) throws IOException {
        return gson.fromJson(getReader(get), type);
    }

    protected InputStreamReader getReader(HttpUriRequest get) throws IOException {
        HttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();
        if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
            throw new HttpResponseException(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(entity, "UTF-8"));
        }
        String encoding = getEncoding(entity);
        return new InputStreamReader(entity.getContent(), encoding);
    }

    private String getEncoding(HttpEntity entity) {
        String encoding = DEFAULT_ENCODING;
        if (entity.getContentEncoding() != null) {
            encoding = entity.getContentEncoding().getValue();
        }
        return encoding;
    }

    public DefaultHttpClient getHttpClient() {
        return httpClient;
    }

    @Inject
    public void setHttpClient(DefaultHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Gson getGson() {
        return gson;
    }

    @Inject
    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
