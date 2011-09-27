package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.localdata.ConfigurationMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @author ytaras
 *         Date: 9/27/11
 *         Time: 4:44 PM
 */
@Singleton
public class ApplicationMenuBar extends JMenuBar {
    public ApplicationMenuBar() {
        JMenu file = new JMenu("&File");
        file.add(new AbstractAction("Set &download path") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                if (configurationMapper.getDownloadPath() != null) {
                    chooser.setCurrentDirectory(new File(configurationMapper.getDownloadPath()));
                }
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(ApplicationMenuBar.this) == JFileChooser.APPROVE_OPTION) {
                    configurationMapper.setDownloadPath(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        file.add(new AbstractAction("E&xit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(file);
    }

    private ConfigurationMapper configurationMapper;

    @Inject
    public void setConfigurationMapper(ConfigurationMapper configurationMapper) {
        this.configurationMapper = configurationMapper;
    }
}
