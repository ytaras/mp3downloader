package com.mostlymusic.downloader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.client.ItemsService;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 2:11 PM
 */
public class GuiceIntegrationTest {

    @Before
    public void setUpProperties() {
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "3128");
    }

    @Test
    public void shouldCreateInjector() throws Exception {
        Injector injector = Guice.createInjector(new DownloaderModule("serviceUrl"), new LocalStorageModule());
        ItemsService instance = injector.getInstance(ItemsService.class);
    }

    @Test
    public void shouldConnectToInternet() throws IOException {
        DefaultHttpClient httpclient = Guice.createInjector(new DownloaderModule("")).getInstance(DefaultHttpClient.class);

        HttpGet req = new HttpGet("https://issues.apache.org:443/");
        HttpResponse rsp = httpclient.execute(req);

        assertThat(rsp.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }

}
