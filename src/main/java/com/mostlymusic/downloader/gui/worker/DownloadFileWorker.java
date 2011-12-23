package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;
import com.mostlymusic.downloader.manager.ConfigurationMapper;
import com.mostlymusic.downloader.manager.ItemManager;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 11:54 AM
 */
public class DownloadFileWorker extends AbstractSwingClientWorker<Void, Long> {
    private Item item;
    private final ItemsService itemsService;
    private final ConfigurationMapper configuration;
    private final ItemManager itemManager;
    private final Logger logger;
    private static final String FILE_DOWNLOADED_FORMAT = "Track '%s' downloaded to '%s'";
    private static final String FILE_DOWNLOAD_STARTED_FORMAT = "Track '%s' download started";
    private Artist artist;

    @Inject
    public DownloadFileWorker(ItemsService itemsService, ConfigurationMapper configuration, ApplicationModel model,
                              ItemManager itemManager, Logger logger) {
        super(model);
        this.itemsService = itemsService;
        this.configuration = configuration;
        this.itemManager = itemManager;
        this.logger = logger;
    }

    @Override
    public Void doInBackground() throws Exception {
        if (null == item) {
            throw new IllegalStateException("Not initialized worker");
        }
        try {
            getApplicationModel().getItemsTableModel().downloadStarted(item);
            getApplicationModel().publishLogStatus(new LogEvent(String.format(FILE_DOWNLOAD_STARTED_FORMAT, item.getLinkTitle())));
            publish(0L);
            final Map.Entry<InputStream, Long> track = itemsService.getTrack(item);
            if (track.getValue() < 0) {
                // We don't know size of entry, so don't bother calculating
                OutputStream outputFile = null;
                try {
                    outputFile = new BufferedOutputStream(getOutputFile());
                    IOUtils.copy(track.getKey(), outputFile);
                } finally {
                    IOUtils.closeQuietly(track.getKey());
                    IOUtils.closeQuietly(outputFile);
                }

            } else {
                // Rock-n-roll
                getApplicationModel().getItemsTableModel().setFileSize(item, track.getValue());
                copy(track.getKey(), getOutputFile(), new StreamCopyListener() {
                    private long bytesWritten = 0;

                    @Override
                    public void bytesWritten(int counter) {
                        bytesWritten += counter;
                        publish(bytesWritten);
                    }
                });
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error downloading", e);
            itemManager.setStatus(item.getItemId(), Item.ERROR);
        }
        itemManager.setStatus(item.getItemId(), Item.DOWNLOADED);
        return null;
    }

    @Override
    protected void process(List<Long> integers) {
        Long integer = integers.get(integers.size() - 1);
        getApplicationModel().getItemsTableModel().setDownloadProgress(item, integer);
    }

    @Override
    protected void doDone(Void aVoid) {
        getApplicationModel().publishLogStatus(new LogEvent(String.format(FILE_DOWNLOADED_FORMAT, item.getLinkTitle(),
                getFile().getAbsolutePath())));
        getApplicationModel().getItemsTableModel().downloadStopped(item);
    }

    @Override
    protected String getErrorMessage() {
        return "Error while downloading '" + item.getLinkTitle() + "': ";
    }

    @Override
    protected void processException(Throwable cause) {
        super.processException(cause);
        getApplicationModel().getItemsTableModel().downloadStopped(item);
    }

    private FileOutputStream getOutputFile() throws FileNotFoundException {
        File file = getFile();
        return new FileOutputStream(file);
    }

    private File getFile() {
        String downloadPath = configuration.getDownloadPath();
        File file = new File(downloadPath);
        String artistPath;
        if (artist == null || artist.getName() == null || artist.getName().isEmpty()) {
            artistPath = "UnknownArtist";
        } else {
            artistPath = artist.getName();
            artistPath = encodeFileName(artistPath);
        }
        file = new File(file, artistPath);
        String productPath;
        if (item.getProductName() == null || item.getProductName().isEmpty()) {
            productPath = "UnknownAlbum";
        } else {
            productPath = item.getProductName();
            productPath = encodeFileName(productPath);
        }
        file = new File(file, productPath);
        //noinspection ResultOfMethodCallIgnored
        file.mkdirs();
        return new File(file, encodeFileName(item.getFileName()));
    }

    private String encodeFileName(String productPath) {
        return productPath.replaceAll("[^\\w ]+", "");
    }

    private static void copy(InputStream from, OutputStream to, StreamCopyListener listener) throws IOException {
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            in = new BufferedInputStream(from);
            out = new BufferedOutputStream(to);
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
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    private interface StreamCopyListener {
        public void bytesWritten(int counter);
    }
}
