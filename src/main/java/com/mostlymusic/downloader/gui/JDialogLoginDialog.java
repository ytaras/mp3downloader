package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.*;

/**
 * @author ytaras
 *         Date: 11/7/11
 *         Time: 1:09 PM
 */
@Singleton
public class JDialogLoginDialog implements LoginDialog {
    private final ApplicationModel applicationModel;

    @Inject
    public JDialogLoginDialog(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    @Override
    public void showDialog(MainWindow window) {
        JDialog dialog = new JDialog(window, "Login", true);
        AccountsList accountsList = new AccountsList();
        accountsList.setModel(applicationModel);
        JPanel contentPane = accountsList.getContentPane();
        dialog.getContentPane().add(contentPane, BorderLayout.CENTER);
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setResizable(true);
        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);
    }
}
