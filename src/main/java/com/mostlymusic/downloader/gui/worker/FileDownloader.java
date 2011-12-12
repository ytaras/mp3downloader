package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.manager.ArtistMapper;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 4:35 PM
 */
public class FileDownloader {
    private final Injector injector;
    private final ArtistMapper artistMapper;

    @Inject
    public FileDownloader(Injector injector, ArtistMapper artistMapper) {
        this.injector = injector;
        this.artistMapper = artistMapper;
    }

    public DownloadFileWorker createWorker(Item item) {
        DownloadFileWorker instance = injector.getInstance(DownloadFileWorker.class);
        instance.setItem(item);
        if (item.getMainArtistId() != 0) {
            instance.setArtist(artistMapper.loadArtist(item.getMainArtistId()));
        }

        return instance;
    }

    public void scheduleDownload(Item item) {
        createWorker(item).execute();
    }
}
