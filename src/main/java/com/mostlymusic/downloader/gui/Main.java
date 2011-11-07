package com.mostlymusic.downloader.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.GuiModule;
import com.mostlymusic.downloader.LocalStorageModule;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        final JFrame frame = new JFrame("MostlyMusic Download Manager");
        frame.setContentPane((Container) injector.getInstance(MainContainer.class).getContentPane());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setJMenuBar(injector.getInstance(JMenuBar.class));
        frame.setGlassPane(injector.getInstance(ProgressGlassPane.class));
        frame.setVisible(true);
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                JOptionPane.showMessageDialog(frame, throwable, "Error occurred", JOptionPane.ERROR_MESSAGE);
                throwable.printStackTrace();
            }
        });
        if (SystemTray.isSupported()) {
            addTray(frame);
        }
    }

    private static void addTray(final JFrame frame) {
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/favicon.jpg"));
        ActionListener showWindowAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setState(JFrame.MAXIMIZED_BOTH);
                frame.toFront();
            }
        };
        PopupMenu popupMenu = getTrayMenu(showWindowAction);

        TrayIcon trayIcon = new TrayIcon(image, "MostlyMusic Download Manager", popupMenu);

        trayIcon.setImageAutoSize(true);
        trayIcon.addActionListener(showWindowAction);
        try {
            systemTray.add(trayIcon);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowIconified(WindowEvent e) {
                    frame.setVisible(false);
                }
            });
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static PopupMenu getTrayMenu(ActionListener showWindowAction) {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem restore = new MenuItem("Restore");
        restore.addActionListener(showWindowAction);
        popupMenu.add(restore);
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popupMenu.add(exit);
        return popupMenu;
    }

}
