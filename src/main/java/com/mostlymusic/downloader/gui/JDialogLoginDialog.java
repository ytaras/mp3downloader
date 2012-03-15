package com.mostlymusic.downloader.gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;

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
        final JDialog dialog = new JDialog(window, "Login", true);
        applicationModel.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(Account account) {
                dialog.setVisible(false);
            }
        });
        AccountsList accountsList = new AccountsList();
        accountsList.setModel(applicationModel);
        JPanel contentPane = accountsList.getContentPane();
        dialog.getContentPane().add(contentPane, BorderLayout.CENTER);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(window);
        dialog.setVisible(true);
    }
}
