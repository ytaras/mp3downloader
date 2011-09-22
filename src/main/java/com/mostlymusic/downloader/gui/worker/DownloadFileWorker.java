package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.Configuration;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 11:54 AM
 */
public class DownloadFileWorker extends SwingWorker<Void, Void> {
    private Item item;
    private ItemsService itemsService;
    private Configuration configuration;
    private ApplicationModel model;
    private static final String FILE_DOWNLOADED_FORMAT = "Track '%s' download finished";
    private static final String FILE_DOWNLOAD_STARTED_FORMAT = "Track '%s' download started";

    @Inject
    public DownloadFileWorker(ItemsService itemsService, Configuration configuration, ApplicationModel model) {
        this.itemsService = itemsService;
        this.configuration = configuration;
        this.model = model;
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (null == item) {
            throw new IllegalStateException("Not initialized worker");
        }
        InputStream track = itemsService.getTrack(item);
        model.publishLogStatus(new LogEvent(String.format(FILE_DOWNLOAD_STARTED_FORMAT, item.getLinkTitle())));
        try {
            IOUtils.copy(track, getOutputFile(item));
        } finally {
            IOUtils.closeQuietly(track);
        }
        return null;
    }

    @Override
    protected void done() {
        model.publishLogStatus(new LogEvent(String.format(FILE_DOWNLOADED_FORMAT, item.getLinkTitle())));
    }

    private FileOutputStream getOutputFile(Item item) throws FileNotFoundException {
        File file = new File(configuration.getDownloadDirectory(), item.getFileName());
        return new FileOutputStream(file);
    }

    public void setItemToDownload(Item item) {
        this.item = item;
    }
}
