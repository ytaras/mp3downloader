package com.mostlymusic.downloader.gui.worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.swing.*;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.manager.ArtistMapper;
import org.jetbrains.annotations.Nullable;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 4:35 PM
 */
@Singleton
public class FileDownloader {
    private final Injector injector;
    private final ArtistMapper artistMapper;
    private final Logger logger;
    private AtomicInteger workingThreads = new AtomicInteger(0);
    private Set<Long> scheduled = Collections.synchronizedSet(new HashSet<Long>());
    private Queue<DownloadFileWorker> waitingQueue = new ConcurrentLinkedQueue<DownloadFileWorker>();
    private int downloadThreadsNumber;
    private Set<Long> downloading = Collections.synchronizedSet(new HashSet<Long>());
    private List<FileDownloaderListener> listeners = new CopyOnWriteArrayList<FileDownloaderListener>();

    @Inject
    // TODO Move all checks if file is downloading or scheduled to here - this will be the registry
    public FileDownloader(Injector injector, ArtistMapper artistMapper, Logger logger) {
        this.injector = injector;
        this.artistMapper = artistMapper;
        this.logger = logger;
    }

    private DownloadFileWorker createWorker(Item item) {
        DownloadFileWorker instance = injector.getInstance(DownloadFileWorker.class);
        Artist artist = null;
        if (item.getMainArtistId() != 0) {
            artist = artistMapper.loadArtist(item.getMainArtistId());
        }
        instance.setDownloadData(item, artist);
        return instance;
    }

    public void scheduleDownload(Item item, @Nullable PropertyChangeListener listener) {
        // TODO This is not thread safe
        if (isScheduled(item) || isDownloading(item)) {
            return;
        }

        final DownloadFileWorker worker = createWorker(item);
        if (listener != null) {
            worker.addPropertyChangeListener(listener);
        }
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                        workerDone(worker);
                    } else if (SwingWorker.StateValue.STARTED == evt.getNewValue()) {
                        workerStarted(worker);
                    }
                }
            }
        });
        scheduled.add(item.getItemId());
        waitingQueue.add(worker);
        fireScheduled(item);
        startWorkers();
    }

    private void fireScheduled(Item itemId) {
        for (FileDownloaderListener listener : listeners) {
            listener.itemScheduled(itemId);
        }
    }

    @Inject
    public void setApplicationModel(ApplicationModel applicationModel) {
    }

    private void workerStarted(DownloadFileWorker worker) {
        Item item = worker.getItem();
        scheduled.remove(item.getItemId());
        downloading.add(item.getItemId());
        for (FileDownloaderListener listener : listeners) {
            listener.itemStartedDownload(item);
        }
    }

    public boolean isDownloading(Item item) {
        return downloading.contains(item.getItemId());
    }

    public boolean isScheduled(Item item) {
        return scheduled.contains(item.getItemId());
    }

    private void startWorkers() {
        if (waitingQueue.isEmpty()) {
            return;
        }
        int currentThreads = workingThreads.get();
        if (currentThreads >= downloadThreadsNumber) {
            return;
        }
        DownloadFileWorker worker = waitingQueue.poll();
        if (worker == null) {
            return;
        }
        if (workingThreads.compareAndSet(currentThreads, currentThreads + 1)) {
            worker.execute();
            startWorkers();
        } else {
            waitingQueue.offer(worker);
        }
    }

    private void workerDone(DownloadFileWorker worker) {
        int i = workingThreads.decrementAndGet();
        if (i < 0) {
            logger.severe("Working threads count went below zero!");
            workingThreads.compareAndSet(i, 0);
        }
        downloading.remove(worker.getItem().getItemId());
        for (FileDownloaderListener listener : listeners) {
            listener.itemDownloaded(worker.getItem());
        }
        startWorkers();
    }


    public void scheduleDownload(Item item) {
        scheduleDownload(item, null);
    }

    public void setDownloadThreadsNumber(int downloadThreadNumber) {
        this.downloadThreadsNumber = downloadThreadNumber;
        startWorkers();
    }

    public void addListener(FileDownloaderListener listener) {
        listeners.add(listener);
    }
}
