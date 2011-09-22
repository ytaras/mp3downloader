package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.Configuration;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 11:54 AM
 */
public class DownloadFileWorker extends SwingWorker<Void, Long> {
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
        final Map.Entry<InputStream, Long> track = itemsService.getTrack(item);
        model.getItemsTableModel().startDownload(item);
        model.publishLogStatus(new LogEvent(String.format(FILE_DOWNLOAD_STARTED_FORMAT, item.getLinkTitle())));
        publish(0L);
        try {
            if (track.getValue() < 0) {
                // We don't know size of entry, so don't bother calculating
                IOUtils.copy(track.getKey(), getOutputFile(item));
            } else {
                // Rock-n-roll
                model.getItemsTableModel().setFileSize(item, track.getValue());
                copy(track.getKey(), getOutputFile(item), new StreamCopyListener() {
                    private long bytesWritten = 0;

                    @Override
                    public void bytesWritten(int counter) {
                        bytesWritten += counter;
                        publish(bytesWritten);
                    }
                });
            }
        } finally {
            IOUtils.closeQuietly(track.getKey());
        }
        return null;
    }

    @Override
    protected void process(List<Long> integers) {
        Long integer = integers.get(integers.size() - 1);
        model.getItemsTableModel().setDownloadProgress(item, integer);
    }

    @Override
    protected void done() {
        model.publishLogStatus(new LogEvent(String.format(FILE_DOWNLOADED_FORMAT, item.getLinkTitle())));
        model.getItemsTableModel().stopDownload(item);
    }

    private FileOutputStream getOutputFile(Item item) throws FileNotFoundException {
        File file = new File(configuration.getDownloadDirectory(), item.getFileName());
        return new FileOutputStream(file);
    }

    public void setItemToDownload(Item item) {
        this.item = item;
    }


    public static void copy(InputStream from, OutputStream to, StreamCopyListener listener) throws IOException {
        BufferedInputStream in = new BufferedInputStream(from);
        BufferedOutputStream out = new BufferedOutputStream(to);
        int counter = 0;
        while (true) {
            int data = in.read();
            if (data == -1) {
                break;
            }
            out.write(data);
            counter++;
            if (counter >= 1024) {
                listener.bytesWritten(counter);
                counter = 0;
            }
        }
        if (counter != 0) {
            listener.bytesWritten(counter);
        }
    }

    private interface StreamCopyListener {
        public void bytesWritten(int counter);
    }
}
