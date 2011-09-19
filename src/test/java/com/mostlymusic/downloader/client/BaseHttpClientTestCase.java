package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.After;
import org.junit.Before;

import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:51 AM
 */
public abstract class BaseHttpClientTestCase {
    protected LocalTestServer localTestServer;

    protected String serverUrl;

    @Before
    public void startServer() throws Exception {
        localTestServer = new LocalTestServer(null, null);
        registerHandler();
        localTestServer.start();
        serverUrl = "http:/" + localTestServer.getServiceAddress();
    }

    protected abstract void registerHandler();

    @After
    public void stopServer() throws Exception {
        localTestServer.stop();
    }

    protected abstract static class JsonHttpHandler implements HttpRequestHandler {
        protected abstract Object getObject(HttpRequest httpRequest);

        @Override
        public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
            String json = new Gson().toJson(getObject(httpRequest));
            httpResponse.setEntity(new StringEntity(json));
        }
    }
}
