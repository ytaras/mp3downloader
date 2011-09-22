package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;

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

    void setStatus(String status);

    void fireExceptionEvent(Exception e);

    ItemsTableModel getItemsTableModel();

    Account getLoggedAccount();

    void fireCheckServerDone();

    void publishLogStatus(LogEvent event);

    void fireLoggedInEvent(Account account);

    void fireLoginFailedEvent(Account account);
}
