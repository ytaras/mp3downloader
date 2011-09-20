package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import org.apache.http.*;
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
    protected Injector injector;
    private Gson gson;

    @Before
    public void startServer() throws Exception {
        localTestServer = new LocalTestServer(null, null);
        registerHandler();
        localTestServer.start();
        serverUrl = "http:/" + localTestServer.getServiceAddress();
        injector = Guice.createInjector(new DownloaderModule(serverUrl));
        gson = injector.getInstance(Gson.class);
    }

    protected abstract void registerHandler();

    @After
    public void stopServer() throws Exception {
        localTestServer.stop();
    }

    protected abstract class JsonHttpHandler implements HttpRequestHandler {
        protected String reason;

        protected abstract Object getObject(HttpEntityEnclosingRequest httpRequest) throws Exception;

        @Override
        public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext)
                throws HttpException, IOException {
            if (!httpRequest.getRequestLine().getMethod().equalsIgnoreCase("POST")) {
                throw new RuntimeException("Should be POST");
            }
            Object object;
            try {
                object = getObject((HttpEntityEnclosingRequest) httpRequest);
            } catch (Exception e) {
                httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                httpResponse.setEntity(new StringEntity(e.getMessage()));
                return;
            }
            String json = gson.toJson(object);
            httpResponse.setEntity(new StringEntity(json, "UTF-8"));
        }
    }
}
