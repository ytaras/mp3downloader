package com.mostlymusic.downloader.gui;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.GuiModule;
import com.mostlymusic.downloader.ManagerModule;
import com.mostlymusic.downloader.manager.LocalStorageModule;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:20 PM
 */
public class Main {
    public static void main(String[] args) throws SQLException, IOException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(createLookAndFeel());
        ImageIO.setUseCache(true);
        ImageIO.setCacheDirectory(createTempDirectory());
        if (args.length == 0) {
            args = new String[]{"http://www.mostlymusic.com/"};
        }
        Injector injector = Guice.createInjector(new LocalStorageModule(true), new DownloaderModule(args[0]),
                new GuiModule(), new ManagerModule());
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

    private static LookAndFeel createLookAndFeel() {
        SynthLookAndFeel synthLookAndFeel = new SynthLookAndFeel();
        try {
            synthLookAndFeel.load(GuiModule.class.getResourceAsStream("/laf.xml"), GuiModule.class);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return synthLookAndFeel;
    }


}
