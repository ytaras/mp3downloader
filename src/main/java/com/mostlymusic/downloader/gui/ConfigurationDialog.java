package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.mostlymusic.downloader.localdata.ConfigurationMapper;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class ConfigurationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner refreshRate;
    private JTextField downloadLocation;
    private JButton button1;
    private final ConfigurationMapper configurationMapper;
    private final ApplicationModel applicationModel;

    @Inject
    public ConfigurationDialog(final ConfigurationMapper configurationMapper, ApplicationModel applicationModel) {
        this.configurationMapper = configurationMapper;
        this.applicationModel = applicationModel;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        new SwingWorker<Void, Void>() {

            @Override
            protected Void doInBackground() throws Exception {
                downloadLocation.setText(configurationMapper.getDownloadPath());
                refreshRate.setValue(configurationMapper.getRefreshRate());
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

    private void onOK() {
        Integer value = (Integer) refreshRate.getValue();
        if (value < 5) {
            JOptionPane.showMessageDialog(this, "Minimal value for refresh rate is 5 mins");
        } else {
            configurationMapper.setRefreshRate(value);
            configurationMapper.setDownloadPath(downloadLocation.getText());
            applicationModel.fireConfigurationChanged();
            dispose();
        }
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
