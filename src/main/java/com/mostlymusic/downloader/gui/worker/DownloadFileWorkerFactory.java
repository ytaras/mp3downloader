package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.Configuration;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 4:35 PM
 */
public class DownloadFileWorkerFactory {
    private ItemsService itemsService;
    private Configuration configuration;
    private ApplicationModel model;

    @Inject
    public DownloadFileWorkerFactory(ItemsService itemsService, Configuration configuration, ApplicationModel model) {
        this.itemsService = itemsService;
        this.configuration = configuration;
        this.model = model;
    }

    public DownloadFileWorker createWorker(Item itemAt) {
        return new DownloadFileWorker(itemsService, configuration, model, itemAt);
    }
}
