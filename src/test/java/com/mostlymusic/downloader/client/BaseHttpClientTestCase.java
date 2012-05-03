package com.mostlymusic.downloader.client;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.inject.Injector;
import com.mostlymusic.downloader.MockInjectors;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.entity.StringEntity;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.After;
import org.junit.Before;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:51 AM
 */
public abstract class BaseHttpClientTestCase {
    LocalTestServer localTestServer;

    String serverUrl;
    Injector injector;
    private Gson gson;

    @Before
    public void startServer() throws Exception {
        // TODO Separate out as GUICE module
        localTestServer = new LocalTestServer(null, null);
        registerHandler();
        localTestServer.start();
        serverUrl = "http:/" + localTestServer.getServiceAddress();
        injector = MockInjectors.downloader(serverUrl);
        gson = injector.getInstance(Gson.class);
    }

    protected abstract void registerHandler();

    @After
    public void stopServer() throws Exception {
        localTestServer.stop();
    }

    protected abstract class JsonHttpHandler<RequestType extends HttpRequest> implements HttpRequestHandler {
        protected String reason;

        protected abstract Object getObject(RequestType httpRequest) throws Exception;

        @Override
        public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext)
                throws HttpException, IOException {
            verifyMethod((RequestType) httpRequest);
            Object object;
            try {
                object = getObject((RequestType) httpRequest);
            } catch (Exception e) {
                httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                httpResponse.setEntity(new StringEntity(e.getMessage()));
                return;
            }
            String json = gson.toJson(object);
            httpResponse.setEntity(new StringEntity(json, "UTF-8"));
        }

        protected void verifyMethod(RequestType httpRequest) {
            if (!httpRequest.getRequestLine().getMethod().equalsIgnoreCase("POST")) {
                throw new RuntimeException("Should be POST");
            }
        }
    }
}
