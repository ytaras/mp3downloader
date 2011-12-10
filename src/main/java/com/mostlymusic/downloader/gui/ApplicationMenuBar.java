package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.manager.ConfigurationMapper;

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
    public ApplicationMenuBar(final ConfigurationMapper configurationMapper, final ApplicationModel model) {
        JMenu file = new JMenu("File");
        file.add(new AbstractAction("Configuration") {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConfigurationDialog(configurationMapper, model).setVisible(true);
            }
        });
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
