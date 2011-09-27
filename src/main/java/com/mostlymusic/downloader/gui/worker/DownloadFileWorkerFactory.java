package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mostlymusic.downloader.dto.Item;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 4:35 PM
 */
public class DownloadFileWorkerFactory {
    private Injector injector;

    @Inject
    public DownloadFileWorkerFactory(Injector injector) {
        this.injector = injector;
    }

    public DownloadFileWorker createWorker(Item itemAt) {
        DownloadFileWorker instance = injector.getInstance(DownloadFileWorker.class);
        instance.setItem(itemAt);
        return instance;
    }
}
