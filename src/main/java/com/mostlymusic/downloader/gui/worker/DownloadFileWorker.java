package com.mostlymusic.downloader.gui.worker;

import java.beans.PropertyChangeListener;

import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.dto.Item;
import org.jetbrains.annotations.Nullable;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public interface DownloadFileWorker {
    void addPropertyChangeListener(PropertyChangeListener propertyChangeListener);

    void execute();

    void setDownloadData(Item item, @Nullable Artist artist);
}
