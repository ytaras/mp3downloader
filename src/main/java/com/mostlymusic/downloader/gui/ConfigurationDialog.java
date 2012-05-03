package com.mostlymusic.downloader.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.gui.components.JSystemFileChooser;
import com.mostlymusic.downloader.manager.ConfigurationMapper;

@Singleton
public class ConfigurationDialog {
    private JPanel contentPane;
    private JSpinner refreshRate;
    private JTextField downloadLocation;
    private JButton button1;
    private JSpinner downloadsNumber;
    private JCheckBox autoDownload;
    private JButton cancelButton;
    private JButton OKButton;
    private final ConfigurationMapper configurationMapper;
    private final ApplicationModel applicationModel;
    private MainContainer frame;

    @Inject
    public ConfigurationDialog(final ConfigurationMapper configurationMapper, ApplicationModel applicationModel) {
        this.configurationMapper = configurationMapper;
        this.applicationModel = applicationModel;
        setUI();
        setContentPane(contentPane);

        MatteBorder border = BorderFactory.createMatteBorder(0, 2, 2, 2, Color.decode("#79ac00"));
        contentPane.setBorder(border);

        loadFromDB();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JSystemFileChooser chooser = new JSystemFileChooser();
                String downloadLocationText = downloadLocation.getText();
                if (!(downloadLocationText == null || downloadLocationText.isEmpty())) {
                    chooser.setCurrentDirectory(new File(downloadLocationText));
                }
                chooser.setFileSelectionMode(JSystemFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(contentPane) == JSystemFileChooser.APPROVE_OPTION) {
                    downloadLocation.setText(chooser.getSelectedFile().getAbsolutePath());
                }

            }
        });
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToDB();
                frame.showPanel(MainContainer.ITEMS);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromDB();
                frame.showPanel(MainContainer.ITEMS);
            }
        });
    }

    private void loadFromDB() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                downloadLocation.setText(configurationMapper.getDownloadPath());
                refreshRate.setValue(configurationMapper.getRefreshRate());
                autoDownload.setSelected(configurationMapper.getAutoDownload());
                downloadsNumber.setValue(configurationMapper.getDownloadThreadsNumber());
            }
        });
    }

    public void saveToDB() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                configurationMapper.setRefreshRate((Integer) refreshRate.getValue());
                configurationMapper.setDownloadPath(downloadLocation.getText());
                configurationMapper.setDownloadThreadsNumber((Integer) downloadsNumber.getValue());
                configurationMapper.setAutoDownload(autoDownload.isSelected());
                applicationModel.fireConfigurationChanged();
            }
        });
    }

    private void createUIComponents() {
        refreshRate = new JSpinner(new SpinnerNumberModel(5, 5, 60, 1));
        downloadsNumber = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
        contentPane = new JPanel();
    }

    private void setUI() {
        contentPane.setBackground(Color.decode("#619400"));
        contentPane.setUI(new BasicPanelUI());
        downloadLocation.setUI(new BasicTextFieldUI());
        downloadLocation.setBackground(Color.WHITE);
        downloadLocation.setForeground(Color.BLACK);
        downloadLocation.setSelectedTextColor(Color.WHITE);
        downloadLocation.setSelectionColor(Color.BLACK);
    }

    public void setFrame(MainContainer frame) {
        this.frame = frame;
    }

    public void setContentPane(JPanel contentPane) {
        this.contentPane = contentPane;
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
