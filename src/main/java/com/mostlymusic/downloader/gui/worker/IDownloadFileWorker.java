package com.mostlymusic.downloader.gui.worker;

import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.dto.Item;

import java.beans.PropertyChangeListener;
import java.util.concurrent.RunnableFuture;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public interface IDownloadFileWorker extends RunnableFuture<Void> {
    void setItem(Item item);

    void setArtist(Artist artist);

    void execute();

    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);
}
