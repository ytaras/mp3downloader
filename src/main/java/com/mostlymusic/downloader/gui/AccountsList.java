package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:22 PM
 */
public class AccountsList {
    private JPanel contentPane;
    private JComboBox usernameComboBox;
    private JPasswordField password;
    private JButton loginButton;
    private JCheckBox rememberPassword;
    private final AbstractAction loginAction;

    public AccountsList() {
        loginAction = new AbstractAction("Login") {
            @Override
            public void actionPerformed(ActionEvent e) {
                this.setEnabled(false);
                contentPane.setEnabled(false);
                String password = new String(AccountsList.this.password.getPassword());
                model.login(usernameComboBox.getSelectedItem().toString(), password, rememberPassword.isSelected());
            }
        };
        loginButton.setAction(loginAction);
        usernameComboBox.getEditor().addActionListener(loginAction);
        password.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "login");
        password.getActionMap().put("login", loginAction);
    }

    private ApplicationModel model;

    public void setModel(ApplicationModel model) {
        this.model = model;
        usernameComboBox.setModel(model.getUserNamesModel());
        updatePasswordField();
        usernameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePasswordField();
            }
        });
        model.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loginFailed(Account account) {
                JOptionPane.showMessageDialog(null, "Failed to login, check your username/password",
                        "Login failed", JOptionPane.ERROR_MESSAGE);
                loginAction.setEnabled(true);
            }
        });
    }

    private void updatePasswordField() {
        Account account = this.model.getAccount((String) usernameComboBox.getSelectedItem());
        if (account != null && account.getPassword() != null) {
            password.setText(account.getPassword());
            rememberPassword.setSelected(true);
        } else {
            password.setText(null);
            rememberPassword.setSelected(false);
        }
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
