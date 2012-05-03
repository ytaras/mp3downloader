package com.mostlymusic.downloader.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.plaf.basic.BasicPanelUI;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.gui.components.BackgroundPanel;
import com.mostlymusic.downloader.gui.components.CloseButton;
import com.mostlymusic.downloader.gui.components.MoveMouseListener;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 12:40 PM
 */
@Singleton
public class MainContainer {
    public static final String ITEMS = "ITEMS";
    public static final String CONFIG = "CONFIG";
    public static final ImageIcon COLLAPSE_ICON = new ImageIcon(MainContainer.class.getResource("/controls/collapse_config.png"));
    public static final ImageIcon EXPAND_ICON = new ImageIcon(MainContainer.class.getResource("/controls/expand_config.png"));
    public static final Image COLLAPSED_BG_ICON = new ImageIcon(MainContainer.class.getResource("/controls/button_background_collapsed.png")).getImage();
    public static final Image EXPANDED_BG_ICON = new ImageIcon(MainContainer.class.getResource("/controls/button_background_expanded.png")).getImage();

    private JPanel container;
    private JList logList;
    private JPanel cardPanel;
    private JSplitPane splitPane;
    private JButton maximizeButton;
    private JButton minimizeButton;
    @SuppressWarnings("UnusedDeclaration")
    private JButton closeButton;
    private JButton configButton;
    @SuppressWarnings("UnusedDeclaration")
    private JPanel buttonBackground;
    private JPanel rightBorder;
    private JPanel leftBorder;


    private final CardLayout layout;
    private final DefaultListModel logListModel;
    private final MaximizeRestoreAction maximizeAction;
    private String selectedPanel;

    @Inject
    public MainContainer(final ConfigurationDialog configurationDialog, Items items, final ApplicationModel model) {
        splitPane.setDividerLocation(0.9);
        layout = (CardLayout) cardPanel.getLayout();
        setItems(items);
        setConfigButton(configurationDialog);
        showPanel(ITEMS);
        logListModel = new DefaultListModel();
        logList.setModel(logListModel);
        model.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(Account account) {
                if (account.isCreated()) {
                    configurationDialog.saveToDB();
                }
                showPanel(ITEMS);
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
        AbstractAction minimizeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFrame().setState(Frame.ICONIFIED);
            }
        };
        minimizeButton.addActionListener(minimizeAction);
        maximizeAction = new MaximizeRestoreAction();
        maximizeButton.addActionListener(maximizeAction);
        maximizeButton.setIcon(maximizeAction.getIcon());
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedPanel.equals(ITEMS)) {
                    showPanel(CONFIG);
                } else {
                    showPanel(ITEMS);
                }
            }
        });
        rightBorder.setUI(new BasicPanelUI());
        rightBorder.setBackground(Color.decode("#79ac00"));
        leftBorder.setUI(new BasicPanelUI());
        leftBorder.setBackground(Color.decode("#79ac00"));

    }

    void showPanel(String panel) {
        if (panel.equals(CONFIG)) {
            configButton.setIcon(COLLAPSE_ICON);
            ((BackgroundPanel) buttonBackground).setImage(EXPANDED_BG_ICON);
        } else {
            configButton.setIcon(EXPAND_ICON);
            ((BackgroundPanel) buttonBackground).setImage(COLLAPSED_BG_ICON);
        }
        layout.show(this.cardPanel, panel);
        this.selectedPanel = panel;
    }

    private void setConfigButton(ConfigurationDialog configurationDialog) {
        cardPanel.add(configurationDialog.getContentPane(), CONFIG);
        configurationDialog.setFrame(this);
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
                    JFrame frame = getFrame();
                    if ((frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == 0) {
                        maximizeFrame(frame);
                    } else {
                        minimizeFrame(frame);
                    }
                }
            }
        });
        MoveMouseListener moveMouseListener = new MoveMouseListener(container);
        container.addMouseListener(moveMouseListener);
        container.addMouseMotionListener(moveMouseListener);
        closeButton = new CloseButton();
        buttonBackground = new BackgroundPanel(COLLAPSED_BG_ICON, BackgroundPanel.Style.SCALED);
    }

    private JFrame getFrame() {
        return (JFrame) SwingUtilities.getRoot(container);
    }

    // TODO Figure out why window listener doesn't work here
    public void reloadMaximizedStatus() {
        maximizeAction.setMaximized((getFrame().getExtendedState() & Frame.MAXIMIZED_BOTH) != 0);
    }

    private class MaximizeRestoreAction extends AbstractAction {
        private boolean maximized;
        private final ImageIcon maximizeIcon;
        private final ImageIcon restoreIcon;

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame frame = getFrame();
            if (!maximized) {
                maximizeFrame(frame);
            } else {
                minimizeFrame(frame);
            }
            maximizeButton.setIcon(getIcon());
        }

        public void setMaximized(boolean maximized) {
            this.maximized = maximized;
        }

        public MaximizeRestoreAction() {
            maximizeIcon = new ImageIcon(getClass().getResource("/controls/maximize_button.png"));
            restoreIcon = new ImageIcon(getClass().getResource("/controls/restore_button.png"));
        }

        public Icon getIcon() {
            if (maximized) {
                return restoreIcon;
            } else {
                return maximizeIcon;
            }
        }
    }

    private void minimizeFrame(JFrame frame) {
        frame.setExtendedState(frame.getExtendedState() & ~Frame.MAXIMIZED_BOTH);
        reloadMaximizedStatus();
    }

    private void maximizeFrame(JFrame frame) {
        frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        reloadMaximizedStatus();
    }
}

