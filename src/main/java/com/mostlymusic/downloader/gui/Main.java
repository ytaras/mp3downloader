package com.mostlymusic.downloader.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.GuiModule;
import com.mostlymusic.downloader.LocalStorageModule;

import java.sql.SQLException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:20 PM
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        if (args.length == 0) {
            args = new String[]{"http://www.mostlymusic.com/"};
        }
        Injector injector = Guice.createInjector(new LocalStorageModule(), new DownloaderModule(args[0]), new GuiModule());
        MainWindow main = injector.getInstance(MainWindow.class);
    }

}
