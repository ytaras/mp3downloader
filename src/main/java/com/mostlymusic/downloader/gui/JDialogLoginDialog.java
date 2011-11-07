package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;

import javax.swing.*;

/**
 * @author ytaras
 *         Date: 11/7/11
 *         Time: 1:09 PM
 */
@Singleton
public class JDialogLoginDialog implements LoginDialog {
    public JDialogLoginDialog() {
    }

    @Override
    public void showDialog(MainWindow window) {
        JDialog dialog = new JDialog(window, "Login", true);
        dialog.getContentPane().add(new JLabel("Hello, world"));
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);
    }
}
