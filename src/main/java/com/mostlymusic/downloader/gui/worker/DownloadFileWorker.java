package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.Configuration;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.dto.Item;
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

    @Inject
    public DownloadFileWorker(ItemsService itemsService, Configuration configuration) {
        this.itemsService = itemsService;
        this.configuration = configuration;
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (null == item) {
            throw new IllegalStateException("Not initialized worker");
        }
        // TODO Service should accept link object, not hash
        InputStream track = itemsService.getTrack(item.getLinkHash());
        try {
            IOUtils.copy(track, getOutputFile(item));
        } finally {
            IOUtils.closeQuietly(track);
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private FileOutputStream getOutputFile(Item item) throws FileNotFoundException {
        File file = new File(configuration.getDownloadDirectory(), item.getFileName());
        return new FileOutputStream(file);
    }

    public void setItemToDownload(Item item) {
        this.item = item;
    }
}
