package com.mostlymusic.downloader.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.GuiModule;
import com.mostlymusic.downloader.LocalStorageModule;
import com.mostlymusic.downloader.localdata.SchemaCreator;

import javax.swing.*;
import java.awt.*;
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
        injector.getInstance(SchemaCreator.class).createTables();
        final JFrame frame = new JFrame("MostlyMusic Download Manager");
        frame.setContentPane((Container) injector.getInstance(MainContainer.class).getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setGlassPane(injector.getInstance(ProgressGlassPane.class));
        frame.setVisible(true);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                JOptionPane.showMessageDialog(frame, throwable, "Error occured", JOptionPane.ERROR_MESSAGE);
                throwable.printStackTrace();
            }
        });
    }

}
