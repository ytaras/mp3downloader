package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;

import java.io.IOException;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/26/11
 *         Time: 3:13 PM
 */
public interface ArtistsService {
    String IDS_PARAM_NAME = "";

    List<Artist> getArtists(List<Long> artistIds) throws IOException, RequestException;
}
