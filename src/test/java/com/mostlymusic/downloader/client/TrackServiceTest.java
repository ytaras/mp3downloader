package com.mostlymusic.downloader.client;

import org.apache.http.localserver.LocalTestServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 3:32 PM
 */
public class TrackServiceTest {

    private LocalTestServer localTestServer;
    private String serverUrl;

    @Before
    public void startServer() throws Exception {
        localTestServer = new LocalTestServer(null, null);
        localTestServer.start();
        serverUrl = "http://" + localTestServer.getServiceHostName() + ":" + localTestServer.getServicePort() + "/";
    }

    @Test
    public void shouldReturnList() {
        // given
        TracksListService tracksListService = new TracksListService();
        tracksListService.setServiceUrl(serverUrl + "tracks");

        // when
        TracksDto dto = tracksListService.getTracks();

        // then
        assertThat(dto).isNotNull();
    }

    @After
    public void stopServer() throws Exception {
        localTestServer.stop();
    }


}
