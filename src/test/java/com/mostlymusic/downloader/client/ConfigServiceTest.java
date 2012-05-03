package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: ytaras
 * Date: 28.04.12
 * Time: 9:40
 */
public class ConfigServiceTest extends BaseHttpClientTestCase {

    private ConfigServiceTest.ConfigRequestHandler handler;

    @Override
    protected void registerHandler() {
        handler = new ConfigRequestHandler();
        localTestServer.register("/download-manager/sync/config/", handler);
    }

    @Test
    public void shouldAskServerForConfig() throws IOException, RequestException {
        ConfigService instance = injector.getInstance(ConfigService.class);
        Config config = instance.getConfig();
        assertThat(config.getMaxPageSize()).isEqualTo(555);
    }

    @Test
    public void shouldCache() throws IOException, RequestException {
        ConfigService instance = injector.getInstance(ConfigService.class);
        assertThat(handler.counter).isZero();
        instance.getConfig();
        assertThat(handler.counter).isEqualTo(1);
        instance.getConfig();
        assertThat(handler.counter).isEqualTo(1);

    }

    private class ConfigRequestHandler extends JsonHttpHandler<HttpRequest> {
        private int counter;

        @Override
        protected void verifyMethod(HttpRequest httpRequest) {
            // No-op
        }

        @Override
        protected Object getObject(HttpRequest httpRequest) throws Exception {
            counter++;
            return new Config(555);
        }
    }
}
