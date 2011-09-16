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
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

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
        HttpRequestHandler tracksHandler = new TracksHttpHandler();
        localTestServer.register("/tracks/", tracksHandler);
        localTestServer.start();
        serverUrl = "http:/" + localTestServer.getServiceAddress();
    }

    @Test
    public void shouldReturnList() throws IOException {
        // given
        TracksListService tracksListService = new TracksListService();
        tracksListService.setServiceUrl(serverUrl + "/tracks/");
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        TracksDto dto = tracksListService.getTracks();

        // then
        assertThat(dto).isEqualTo(getMockDto());
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    private TracksDto getMockDto() {
        TracksDto tracksDto = new TracksDto();
        tracksDto.setTracks(new LinkedList<TrackDto>());
        TrackDto trackDto = new TrackDto();
        trackDto.setId(1);
        trackDto.setAlbum("Album");
        trackDto.setName("Name");
        trackDto.setArtist("Artist");
        tracksDto.getTracks().add(trackDto);
        return tracksDto;
    }


    @After
    public void stopServer() throws Exception {
        localTestServer.stop();
    }


    private class TracksHttpHandler implements HttpRequestHandler {
        @Override
        public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
            Gson gson = new Gson();
            httpResponse.setEntity(new StringEntity(gson.toJson(getMockDto())));
        }


    }
}
