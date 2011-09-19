package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.swing.*;

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

    public static void main(String[] args) {
        JFrame frame = new JFrame("AccountsList");
        frame.setContentPane(new AccountsList().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
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
