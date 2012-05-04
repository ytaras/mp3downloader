package com.mostlymusic.downloader.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.gui.components.ComponentResizer;
import com.mostlymusic.downloader.manager.ConfigurationMapper;
import com.mostlymusic.downloader.manager.FrameSize;

import static java.lang.Math.floor;

/**
 * @author ytaras
 *         Date: 11/7/11
 *         Time: 1:02 PM
 */
@Singleton
public class MainWindow extends JFrame {

    @Inject
    public MainWindow(ApplicationModel applicationModel, JMenuBar menuBar, ProgressGlassPane progressGlassPane,
                      MainContainer mainContainer, LoginDialog loginDialog, final ConfigurationMapper configurationMapper)
            throws HeadlessException {
        super("MostlyMusic Download Manager");
        Container contentPane = mainContainer.getContentPane();
        setContentPane(contentPane);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Dimension size = MainWindow.this.getSize();
                configurationMapper.setFrameSize(new FrameSize(size));
                System.exit(0);
            }
        });
        setJMenuBar(menuBar);
        menuBar.setVisible(false);
        setGlassPane(progressGlassPane);
        setUndecorated(true);
        ComponentResizer componentResizer = new ComponentResizer();
        componentResizer.setSnapSize(new Dimension(10, 10));
        componentResizer.registerComponent(this);
        getRootPane().setBorder(new LineBorder(getBackground(), 2));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setMinimumSize(new Dimension(600, (int) Math.min(floor(screenSize.getWidth()), 850)));
        pack();
        setSize(configurationMapper.getFrameSize().getDimension());

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
                    addTray(MainWindow.this, configurationMapper);
                }
            }.start();
        }
        applicationModel.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(Account account) {
                setVisible(true);
                setState(NORMAL);
            }
        });
        loginDialog.showDialog(this);
        setIconImages(getIcons());
    }

    private static void addTray(final JFrame frame, ConfigurationMapper configurationMapper) {
        SystemTray systemTray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/windows7_icon3232.png"));
        ActionListener showWindowAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setExtendedState(frame.getExtendedState() & ~Frame.ICONIFIED);
                        frame.setVisible(true);
                        frame.toFront();
                    }
                });
            }
        };
        PopupMenu popupMenu = getTrayMenu(showWindowAction, frame, configurationMapper);

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

    private static PopupMenu getTrayMenu(ActionListener showWindowAction, final Frame frame, final ConfigurationMapper configurationMapper) {
        PopupMenu popupMenu = new PopupMenu();
        final MenuItem restore = new MenuItem("Restore");
        restore.addActionListener(showWindowAction);
        popupMenu.add(restore);
        final MenuItem hide = new MenuItem("Hide");
        hide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        frame.setExtendedState(frame.getExtendedState() | Frame.ICONIFIED);
                    }
                });
            }
        });
        popupMenu.add(hide);
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Dimension size = frame.getSize();
                configurationMapper.setFrameSize(new FrameSize(size));
                System.exit(0);
            }
        });
        popupMenu.add(exit);
        return popupMenu;
    }

    public static List<Image> getIcons() {
        return Arrays.asList(new ImageIcon(MainWindow.class.getResource("/windows7_icon.png")).getImage(),
                new ImageIcon(MainWindow.class.getResource("/windows7_icon1616.png")).getImage(),
                new ImageIcon(MainWindow.class.getResource("/windows7_icon3232.png")).getImage());
    }
}
