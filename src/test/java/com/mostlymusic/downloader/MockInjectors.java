package com.mostlymusic.downloader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;

/**
 * @author ytaras
 *         Date: 11/10/11
 *         Time: 10:51 AM
 */
public class MockInjectors {
    private static Module FULL_TEMP_DB_MODULE = Modules.override(new GuiModule(),
            new DownloaderModule(""), new LocalStorageModule()).with(new TempDbModule());
    private static Module STORAGE_TEMP_DB_MODULE = Modules.override(new LocalStorageModule())
            .with(new TempDbModule());

    public static Injector FULL_TEMP_DB = Guice.createInjector(FULL_TEMP_DB_MODULE);
    private static final Injector STORAGE_TEMP_DB = Guice.createInjector(STORAGE_TEMP_DB_MODULE);

    public static Injector storageTempDb(boolean createNew) {
        if (createNew) {
            return Guice.createInjector(STORAGE_TEMP_DB_MODULE);
        } else {
            return STORAGE_TEMP_DB;
        }
    }

    public static Injector downloader(String serverUrl) {
        return Guice.createInjector(new DownloaderModule(serverUrl));
    }
}
