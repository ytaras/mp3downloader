package com.mostlymusic.downloader.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.gui.components.BackgroundPanel;
import com.mostlymusic.downloader.gui.components.MoveMouseListener;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 12:40 PM
 */
@Singleton
public class MainContainer {
    private static final String ITEMS = "ITEMS";

    private JPanel container;
    private JList logList;
    private JPanel cardPanel;
    private JSplitPane splitPane;

    private final CardLayout layout;
    private final DefaultListModel logListModel;

    @Inject
    public MainContainer(Items items, final ApplicationModel model) {
        splitPane.setDividerLocation(0.9);
        layout = (CardLayout) cardPanel.getLayout();
        setItems(items);
        layout.show(this.cardPanel, ITEMS);
        logListModel = new DefaultListModel();
        logList.setModel(logListModel);
        model.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(Account account) {
                layout.show(cardPanel, ITEMS);
            }

            @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
            @Override
            public void logEvent(LogEvent event) {
                Throwable exception = event.getException();
                if (exception != null) {
                    if (exception.getMessage() == null || exception.getMessage().isEmpty()) {
                        logListModel.add(0, event.toString() + " " + exception.getClass().getSimpleName());
                    } else {
                        logListModel.add(0, event + " " + exception.getMessage());
                    }
                } else {
                    logListModel.add(0, event);
                }
            }

            @Override
            public void exceptionOccurred(Throwable e) {
                model.publishLogStatus(new LogEvent("Uncaught exception:", e));
            }
        });
        model.publishLogStatus(new LogEvent("Started application"));
    }

    public Container getContentPane() {
        return container;
    }

    private void setItems(Items items) {
        cardPanel.add(items.getContentPane(), ITEMS);
    }

    private void createUIComponents() throws IOException {
        final int imageSize = 31;
        final Color color = Color.decode("#273f32");

        container = new BackgroundPanel("/headers/main_window.jpg", BackgroundPanel.Style.ACTUAL) {

            @Override
            protected void beforeDrawImage(Graphics g) {
                g.setColor(color);
                g.fillRect(0, 0, this.getWidth(), imageSize);
            }
        };
        container.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() >= 2 &&
                        mouseEvent.getPoint().getY() <= ((BackgroundPanel) container).getImage().getHeight(null)) {
                    JFrame frame = (JFrame) SwingUtilities.getRoot(container);
                    if ((frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0) {
                        frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
                    } else {
                        frame.setExtendedState(frame.getExtendedState() & ~Frame.MAXIMIZED_BOTH);
                    }
                }
            }
        });
        MoveMouseListener moveMouseListener = new MoveMouseListener(container);
        container.addMouseListener(moveMouseListener);
        container.addMouseMotionListener(moveMouseListener);
    }
}
