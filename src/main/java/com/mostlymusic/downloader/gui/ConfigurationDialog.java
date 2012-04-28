package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.manager.ConfigurationMapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

@Singleton
public class ConfigurationDialog extends JDialog {
    // TODO COnvert to panel fully
    private JPanel contentPane;
    private JSpinner refreshRate;
    private JTextField downloadLocation;
    private JButton button1;
    private JSpinner downloadsNumber;
    private JCheckBox autoDownload;
    private final ConfigurationMapper configurationMapper;
    private final ApplicationModel applicationModel;

    @Inject
    public ConfigurationDialog(final ConfigurationMapper configurationMapper, ApplicationModel applicationModel) {
        this.configurationMapper = configurationMapper;
        this.applicationModel = applicationModel;
        applicationModel.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(Account account) {
                if (account.isCreated()) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            setVisible(true);
                        }
                    });

                }
            }
        });
        setContentPane(contentPane);
        setModal(true);
// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                downloadLocation.setText(configurationMapper.getDownloadPath());
                refreshRate.setValue(configurationMapper.getRefreshRate());
                autoDownload.setSelected(configurationMapper.getAutoDownload());
                downloadsNumber.setValue(configurationMapper.getDownloadThreadsNumber());
                return null;
            }
        }.execute();
        pack();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                String downloadLocationText = downloadLocation.getText();
                if (!(downloadLocationText == null || downloadLocationText.isEmpty())) {
                    chooser.setCurrentDirectory(new File(downloadLocationText));
                }
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if (chooser.showOpenDialog(ConfigurationDialog.this) == JFileChooser.APPROVE_OPTION) {
                    downloadLocation.setText(chooser.getSelectedFile().getAbsolutePath());
                }

            }
        });
    }

    public void onOK() {
        configurationMapper.setRefreshRate((Integer) refreshRate.getValue());
        configurationMapper.setDownloadPath(downloadLocation.getText());
        configurationMapper.setDownloadThreadsNumber((Integer) downloadsNumber.getValue());
        configurationMapper.setAutoDownload(autoDownload.isSelected());
        applicationModel.fireConfigurationChanged();
        dispose();
    }

    private void createUIComponents() {
        refreshRate = new JSpinner(new SpinnerNumberModel(5, 5, 60, 1));
        downloadsNumber = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    }
}
