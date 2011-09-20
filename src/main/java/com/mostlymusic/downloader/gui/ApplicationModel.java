package com.mostlymusic.downloader.gui;

import com.sun.istack.internal.Nullable;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:37 PM
 */
public interface ApplicationModel {
    AccountTableModel getAccountsTableModel();

    void createAccount(String text, char[] password);

    void loginToAccountAt(int selectedRow);

    void addListener(ApplicationModelListener mock);

    void setStatus(@Nullable String status);
}
