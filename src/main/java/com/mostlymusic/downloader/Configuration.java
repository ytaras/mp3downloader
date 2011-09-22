package com.mostlymusic.downloader;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 12:15 PM
 */
@Singleton
public class Configuration {
    private File downloadDirectory;

    @Inject
    public Configuration(@DownloadDirectory File downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }

    public File getDownloadDirectory() {
        return downloadDirectory;
    }

    public void setDownloadDirectory(File downloadDirectory) {
        this.downloadDirectory = downloadDirectory;
    }
}
