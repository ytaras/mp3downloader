package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.localdata.ConnectionManager;
import com.mostlymusic.downloader.localdata.IConnectionManager;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:02 PM
 */
public class LocalStorageModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IConnectionManager.class).to(ConnectionManager.class);
    }
}
