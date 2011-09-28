package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:37 PM
 */
public interface ApplicationModel {
    AccountTableModel getAccountsTableModel();

    void addListener(ApplicationModelListener mock);

    void setStatus(String status);

    void fireExceptionEvent(Throwable e);

    ItemsTableModel getItemsTableModel();

    Account getLoggedAccount();

    void fireCheckServerDone();

    void publishLogStatus(LogEvent event);

    void fireLoggedInEvent(Account account);

    void fireLoginFailedEvent(Account account);

    void login(String login, String password);
}
