package com.mostlymusic.downloader.client;

import org.apache.http.localserver.LocalTestServer;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 3:32 PM
 */
public class TrackServiceTest {

    private LocalTestServer localTestServer;
    private HttpRequestHandler tracksHandler;
    private String serverUrl;

    @Before
    public void startServer() throws Exception {
        localTestServer = new LocalTestServer(null, null);
        tracksHandler = mock(HttpRequestHandler.class);
        localTestServer.start();
        serverUrl = "http:/" + localTestServer.getServiceAddress() + "/";
    }

    @Test
    public void shouldReturnList() throws IOException {
        // given
        TracksListService tracksListService = new TracksListService();
        tracksListService.setServiceUrl(serverUrl + "tracks");
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        TracksDto dto = tracksListService.getTracks();

        // then
        assertThat(dto).isNotNull();
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @After
    public void stopServer() throws Exception {
        localTestServer.stop();
    }


}
