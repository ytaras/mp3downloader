package com.mostlymusic.downloader.gui;

import javax.swing.table.TableModel;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:37 PM
 */
public interface ApplicationModel {
    TableModel getAccountsTableModel();

    void createAccount(String text, char[] password);
}
