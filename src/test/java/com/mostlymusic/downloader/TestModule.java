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
public class TestModule {
    public static Module INSTANCE = Modules.override(new GuiModule(),
            new DownloaderModule(""), new LocalStorageModule()).with(new MocksModule());
    public static Injector INJECTOR = Guice.createInjector(INSTANCE);
}
