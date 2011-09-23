package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.mostlymusic.downloader.client.exceptions.ForbiddenException;
import com.mostlymusic.downloader.client.exceptions.NotFoundException;
import com.mostlymusic.downloader.client.exceptions.RequestException;
import com.mostlymusic.downloader.client.exceptions.UnauthorizedException;
import org.apache.commons.io.IOUtils;
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

    protected <T> T getResult(HttpUriRequest get, Class<T> aClass) throws IOException, RequestException {
        InputStreamReader reader = null;
        try {
            reader = getReader(get);
            return gson.fromJson(reader, aClass);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    protected <T> T getResult(HttpUriRequest get, Type type) throws IOException, RequestException {
        InputStreamReader reader = null;
        try {
            reader = getReader(get);
            return gson.fromJson(reader, type);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    protected InputStreamReader getReader(HttpUriRequest get) throws IOException, RequestException {
        HttpResponse response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();
        verifyStatus(response);
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

    protected void verifyStatus(HttpResponse response) throws IOException, RequestException {
        final int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        if (HttpStatus.SC_UNAUTHORIZED == statusCode) {
            throw new UnauthorizedException(EntityUtils.toString(entity));
        } else if (HttpStatus.SC_NOT_FOUND == statusCode) {
            throw new NotFoundException(EntityUtils.toString(entity));
        } else if (HttpStatus.SC_FORBIDDEN == statusCode) {
            throw new ForbiddenException(EntityUtils.toString(entity));
        } else if (HttpStatus.SC_GONE == statusCode) {
            throw new ExpiredException(EntityUtils.toString(entity));
        } else if (HttpStatus.SC_PAYMENT_REQUIRED == statusCode) {
            throw new PaymentRequired(EntityUtils.toString(entity));
        } else if (HttpStatus.SC_INTERNAL_SERVER_ERROR == statusCode) {
            throw new InternalServerErrrorException(EntityUtils.toString(entity));
        } else if (HttpStatus.SC_OK != statusCode) {
            throw new HttpResponseException(response.getStatusLine().getStatusCode(), EntityUtils.toString(entity, "UTF-8"));
        }
    }
}
