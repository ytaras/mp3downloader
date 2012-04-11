package com.mostlymusic.downloader.gui.worker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;
import javax.swing.*;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.ApplicationModelListenerAdapter;
import com.mostlymusic.downloader.manager.ArtistMapper;
import com.mostlymusic.downloader.manager.ConfigurationMapper;
import org.jetbrains.annotations.Nullable;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 4:35 PM
 */
public class FileDownloader {
    private final Injector injector;
    private final ArtistMapper artistMapper;
    private final Logger logger;
    private AtomicInteger workingThreads = new AtomicInteger(0);
    private Queue<DownloadFileWorker> waitingQueue = new ConcurrentLinkedQueue<DownloadFileWorker>();
    private int downloadThreadsNumber;

    @Inject
    public FileDownloader(Injector injector, ArtistMapper artistMapper, Logger logger,
                          final ConfigurationMapper mapper, ApplicationModel model) {
        this.injector = injector;
        this.artistMapper = artistMapper;
        this.logger = logger;
        downloadThreadsNumber = mapper.getDownloadThreadsNumber();
        model.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void configurationChanged() {
                downloadThreadsNumber = mapper.getDownloadThreadsNumber();
            }
        });
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
        DownloadFileWorker worker = createWorker(item);
        if (listener != null) {
            worker.addPropertyChangeListener(listener);
        }
        worker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("state".equals(evt.getPropertyName())) {
                    if (SwingWorker.StateValue.DONE == evt.getNewValue()) {
                        workerDone();
                    }
                }
            }
        });
        waitingQueue.add(worker);
        startWorkers();
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

    private void workerDone() {
        int i = workingThreads.decrementAndGet();
        if (i < 0) {
            logger.severe("Working threads count went below zero!");
            workingThreads.compareAndSet(i, 0);
        }
        startWorkers();
    }


    public void scheduleDownload(Item item) {
        scheduleDownload(item, null);
    }
}
