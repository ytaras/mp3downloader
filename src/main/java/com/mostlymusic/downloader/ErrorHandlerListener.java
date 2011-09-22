package com.mostlymusic.downloader;

import com.google.inject.Inject;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.ApplicationModelListenerAdapter;
import com.mostlymusic.downloader.gui.MainContainer;
import com.mostlymusic.downloader.gui.MainLayout;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ytaras
 *         Date: 9/21/11
 *         Time: 9:21 AM
 */
public class ErrorHandlerListener extends ApplicationModelListenerAdapter {

    private Component root;
    private Logger logger;

    @Inject
    public ErrorHandlerListener(ApplicationModel model, @MainLayout MainContainer panel, Logger logger) {
        this.logger = logger;
        model.addListener(this);
        root = SwingUtilities.getRoot(panel.getContentPane());
    }

    @Override
    public void exceptionOccurred(Exception e) {
        JOptionPane.showMessageDialog(root, e, "Error occured", JOptionPane.ERROR_MESSAGE);
        logger.log(Level.SEVERE, "Exception", e);
    }
}
