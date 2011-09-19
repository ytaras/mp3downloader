package com.mostlymusic.downloader.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.GuiModule;
import com.mostlymusic.downloader.LocalStorageModule;

import javax.swing.*;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:20 PM
 */
public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new LocalStorageModule(), new DownloaderModule(""), new GuiModule());

        JFrame frame = new JFrame("AccountsList");
        frame.setContentPane(injector.getInstance(AccountsList.class).getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }
}