package com.mostlymusic.downloader.gui;

import javax.swing.*;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:22 PM
 */
public class AccountsList {
    private JTable accountsTable;
    private JPanel panel1;
    private JButton newAccount;
    private JButton deleteAccount;

    public static void main(String[] args) {
        JFrame frame = new JFrame("AccountsList");
        frame.setContentPane(new AccountsList().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
