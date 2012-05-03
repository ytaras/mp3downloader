package com.mostlymusic.downloader.client;

import java.io.IOException;
import java.util.List;

import com.mostlymusic.downloader.client.exceptions.RequestException;

/**
 * @author ytaras
 *         Date: 9/26/11
 *         Time: 3:13 PM
 */
public interface ArtistsService {
    String IDS_PARAM_NAME = "artist_ids";

    List<Artist> getArtists(List<Long> artistIds) throws IOException, RequestException;
}
