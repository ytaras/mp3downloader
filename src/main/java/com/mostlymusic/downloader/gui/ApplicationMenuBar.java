package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author ytaras
 *         Date: 9/27/11
 *         Time: 4:44 PM
 */
@Singleton
public class ApplicationMenuBar extends JMenuBar {
    @Inject
    public ApplicationMenuBar(final ConfigurationDialog dialog) {
        JMenu file = new JMenu("File");
        file.addSeparator();
        file.add(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(file);
    }
}
