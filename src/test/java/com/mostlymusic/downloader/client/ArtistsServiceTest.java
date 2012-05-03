package com.mostlymusic.downloader.client;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/26/11
 *         Time: 3:11 PM
 */
public class ArtistsServiceTest extends BaseHttpClientTestCase {

    private ArtistsService artistsService;

    @Override
    protected void registerHandler() {
        localTestServer.register("/download-manager/sync/artistsInfo/", new ArtistsRequestHandler());
    }

    @Before
    public void setUp() throws Exception {
        artistsService = injector.getInstance(ArtistsService.class);
    }

    @Test
    public void shouldSelectArtists() throws Exception {
        // given

        // when
        List<Artist> artistList = artistsService.getArtists(Arrays.asList(2L, 5L));

        // then
        assertThat(artistList).isEqualTo(getMockArtists(2L, 5L));
    }

    private List<Artist> getMockArtists(Long... ids) {
        return getMockArtists(Arrays.<Long>asList(ids));
    }

    private List<Artist> getMockArtists(List<Long> ids) {
        List<Artist> artists = new LinkedList<Artist>();
        for (long id : ids) {
            Artist artist = new Artist();
            artist.setArtistId(id);
            artist.setName("" + id);
            artists.add(artist);
        }
        return artists;
    }


    private class ArtistsRequestHandler extends JsonHttpHandler<HttpEntityEnclosingRequest> {
        @Override
        protected Object getObject(HttpEntityEnclosingRequest httpRequest) throws Exception {
            List<NameValuePair> parse = URLEncodedUtils.parse(httpRequest.getEntity());
            List<Long> ids = null;
            for (NameValuePair nameValuePair : parse) {
                if (nameValuePair.getName().equals(ArtistsService.IDS_PARAM_NAME)) {
                    ids = new LinkedList<Long>();
                    String[] split = nameValuePair.getValue().split(",");
                    for (String strId : split) {
                        ids.add(Long.parseLong(strId));
                    }
                }
            }
            if (null == ids) {
                throw new RuntimeException("Ids should be set");
            }
            return getMockArtists(ids);
        }

    }

}
