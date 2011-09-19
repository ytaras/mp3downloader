package com.mostlymusic.downloader.gui;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:37 PM
 */
public interface ApplicationModel {
    AccountTableModel getAccountsTableModel();

    void createAccount(String text, char[] password);
}
