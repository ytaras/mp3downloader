package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:22 PM
 */
@Singleton
public class AccountsList {
    private JTable accountsTable;
    private JPanel contentPane;
    private JButton newAccount;
    private JButton deleteAccount;
    private JButton login;

    public AccountsList() {
        accountsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        accountsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                boolean rowSelected = accountsTable.getSelectedRow() >= 0;
                deleteAccount.setEnabled(rowSelected);
                login.setEnabled(rowSelected);
            }
        });
        newAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JDialog addAccount = new AddAccount(model);
                addAccount.pack();
                addAccount.setLocationRelativeTo(contentPane);
                addAccount.setVisible(true);
            }
        });
        deleteAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int result = JOptionPane.
                        showConfirmDialog(contentPane, "Are you sure?", "Are you sure to delete this account", JOptionPane.YES_NO_OPTION);
                if (JOptionPane.YES_OPTION == result) {
                    model.getAccountsTableModel().deleteAccountAt(accountsTable.getSelectedRow());
                }
            }
        });
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                model.loginToAccountAt(accountsTable.getSelectedRow());
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
        accountsTable.setModel(model.getAccountsTableModel());
    }

    public JPanel getContentPane() {
        return contentPane;
    }
}
