package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.localdata.ConfigurationMapper;

import javax.swing.*;
import java.awt.event.*;

public class ConfigurationDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner refreshRate;
    private JTextField downloadLocation;
    private JButton button1;
    private final ConfigurationMapper configurationMapper;
    private final ApplicationModel applicationModel;

    public ConfigurationDialog(ConfigurationMapper configurationMapper, ApplicationModel applicationModel) {
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
        refreshRate.setValue(configurationMapper.getRefreshRate());
        pack();
    }

    private void onOK() {
        Integer value = (Integer) refreshRate.getValue();
        if (value < 5) {
            JOptionPane.showMessageDialog(this, "Minimal value for refresh rate is 5 mins");
        } else {
            configurationMapper.setRefreshRate(value);
            applicationModel.fireConfigurationChanged();
            dispose();
        }
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
