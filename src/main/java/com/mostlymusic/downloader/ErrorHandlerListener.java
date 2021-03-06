package com.mostlymusic.downloader;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

import com.google.inject.Inject;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.ApplicationModelListenerAdapter;
import com.mostlymusic.downloader.gui.MainContainer;
import com.mostlymusic.downloader.gui.MainLayout;

/**
 * @author ytaras
 *         Date: 9/21/11
 *         Time: 9:21 AM
 */
public class ErrorHandlerListener extends ApplicationModelListenerAdapter {

    private final Component root;
    private final Logger logger;

    @Inject
    public ErrorHandlerListener(ApplicationModel model, @MainLayout MainContainer panel, Logger logger) {
        this.logger = logger;
        model.addListener(this);
        root = SwingUtilities.getRoot(panel.getContentPane());
    }

    @Override
    public void exceptionOccurred(Throwable e) {
        logger.log(Level.SEVERE, "Exception", e);
    }
}
