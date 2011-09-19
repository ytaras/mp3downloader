package com.mostlymusic.downloader.client;

import org.apache.http.HttpRequest;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 3:32 PM
 */
public class TrackServiceTest extends BaseHttpClientTestCase {

    @Override
    protected void registerHandler() {
        HttpRequestHandler tracksHandler = new TracksHttpHandler();
        localTestServer.register("/tracks/", tracksHandler);
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



    private class TracksHttpHandler extends JsonHttpHandler{
        @Override
        protected Object getObject(HttpRequest httpRequest) {
            return getMockDto();
        }
    }
}
