package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author ytaras
 *         Date: 11/7/11
 *         Time: 1:02 PM
 */
@Singleton
public class MainWindow extends JFrame {

    @Inject
    public MainWindow(ApplicationModel applicationModel, JMenuBar menuBar, ProgressGlassPane progressGlassPane,
                      MainContainer mainContainer, LoginDialog loginDialog) throws HeadlessException {
        super("MostlyMusic Download Manager");
        Container contentPane = mainContainer.getContentPane();
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(menuBar);
        setGlassPane(progressGlassPane);
        pack();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                JOptionPane.showMessageDialog(MainWindow.this, throwable, "Error occurred", JOptionPane.ERROR_MESSAGE);
                throwable.printStackTrace();
            }
        });
        if (SystemTray.isSupported()) {
            new Thread() {
                @Override
                public void run() {
                    addTray(MainWindow.this);
                }
            }.start();
        }
        applicationModel.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(Account account) {
                setVisible(true);
                setExtendedState(MAXIMIZED_BOTH);
            }
        });
        loginDialog.showDialog(this);
    }

    private static void addTray(final JFrame frame) {
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/favicon.jpg"));
        ActionListener showWindowAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setVisible(true);
                        frame.setState(JFrame.MAXIMIZED_BOTH);
                        frame.toFront();
                    }
                });
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
