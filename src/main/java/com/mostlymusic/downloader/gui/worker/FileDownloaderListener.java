package com.mostlymusic.downloader.gui.worker;

import com.mostlymusic.downloader.dto.Item;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public interface FileDownloaderListener {
    void itemScheduled(Item id);

    void itemStartedDownload(Item id);

    void itemDownloaded(Item id);
}
