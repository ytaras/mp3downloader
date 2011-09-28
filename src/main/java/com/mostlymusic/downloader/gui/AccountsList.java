package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:22 PM
 */
@Singleton
public class AccountsList {
    private JPanel contentPane;
    private JComboBox usernameComboBox;
    private JPasswordField passwordPasswordField;
    private JButton loginButton;

    public AccountsList() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String password = new String(passwordPasswordField.getPassword());
                model.login(usernameComboBox.getSelectedItem().toString(), password);
            }
        });
    }

    private ApplicationModel model;

    public ApplicationModel getModel() {
        return model;
    }

    @Inject
    public void setModel(ApplicationModel model) {
        this.model = model;
        model.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loginFailed(Account account) {
                JOptionPane.showMessageDialog(null, "Failed to login, check your username/password",
                        "Login failed", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
