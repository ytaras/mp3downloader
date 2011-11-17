package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;
import com.mostlymusic.downloader.localdata.ConfigurationMapper;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 11:54 AM
 */
public class DownloadFileWorker extends AbstractSwingClientWorker<Void, Long> {
    private static final String FILENAME_CHARSET = "UTF-8";
    private Item item;
    private final ItemsService itemsService;
    private final ConfigurationMapper configuration;
    private static final String FILE_DOWNLOADED_FORMAT = "Track '%s' downloaded to '%s'";
    private static final String FILE_DOWNLOAD_STARTED_FORMAT = "Track '%s' download started";
    private Artist artist;

    @Inject
    public DownloadFileWorker(ItemsService itemsService, ConfigurationMapper configuration, ApplicationModel model) {
        super(model);
        this.itemsService = itemsService;
        this.configuration = configuration;
    }

    @Override
    protected Void doInBackground() throws Exception {
        if (null == item || null == artist) {
            throw new IllegalStateException("Not initialized worker");
        }
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
        return null;
    }

    @Override
    protected void process(List<Long> integers) {
        Long integer = integers.get(integers.size() - 1);
        getApplicationModel().getItemsTableModel().setDownloadProgress(item, integer);
    }

    @Override
    protected void doDone(Void aVoid) {
        try {
            getApplicationModel().publishLogStatus(new LogEvent(String.format(FILE_DOWNLOADED_FORMAT, item.getLinkTitle(),
                    getFile().getAbsolutePath())));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        getApplicationModel().getItemsTableModel().downloadStopped(item);
    }

    @Override
    protected String getErrorMessage(Throwable cause) {
        return "Error while downloading '" + item.getLinkTitle() + "': ";
    }

    @Override
    protected void processException(Throwable cause) {
        super.processException(cause);
        getApplicationModel().getItemsTableModel().downloadStopped(item);
    }

    private FileOutputStream getOutputFile() throws FileNotFoundException, UnsupportedEncodingException {
        File file = getFile();
        return new FileOutputStream(file);
    }

    private File getFile() throws UnsupportedEncodingException {
        String downloadPath = configuration.getDownloadPath();
        File file = new File(downloadPath);
        if (!file.mkdirs()) {
            throw new AssertionError();
        }
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
        if (!file.mkdirs()) {
            throw new AssertionError();
        }
        return new File(file, encodeFileName(item.getFileName()));
    }

    private String encodeFileName(String productPath) throws UnsupportedEncodingException {
        return productPath.replaceAll("[^\\w ]+", "");
    }

    public static void copy(InputStream from, OutputStream to, StreamCopyListener listener) throws IOException {
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
