package com.mostlymusic.downloader.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.GuiModule;
import com.mostlymusic.downloader.ManagerModule;
import com.mostlymusic.downloader.manager.LocalStorageModule;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:20 PM
 */
public class Main {
    public static void main(String[] args) throws SQLException, IOException, UnsupportedLookAndFeelException {
        ImageIO.setUseCache(true);
        ImageIO.setCacheDirectory(createTempDirectory());
        if (args.length == 0) {
            args = new String[]{"http://www.mostlymusic.com/"};
        }
        Injector injector = Guice.createInjector(new LocalStorageModule(true), new DownloaderModule(args[0]),
                new GuiModule(), new ManagerModule());
        UIManager.setLookAndFeel(injector.getInstance(LookAndFeel.class));
        injector.getInstance(MainWindow.class);
    }

    private static File createTempDirectory()
            throws IOException {
        final File temp;
        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));
        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }
        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }
        return (temp);
    }

}
