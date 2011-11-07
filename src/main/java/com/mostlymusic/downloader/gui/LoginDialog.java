package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;

/**
 * @author ytaras
 *         Date: 11/7/11
 *         Time: 1:09 PM
 */
@Singleton
public class LoginDialog extends JDialog {
    @Inject
    public LoginDialog() {
        setModal(true);
        getContentPane().add(new JLabel("Some text"));
        pack();
        setVisible(true);
    }
}
