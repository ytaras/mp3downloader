package com.mostlymusic.downloader.gui;

import javax.swing.*;

import com.mostlymusic.downloader.dto.Account;
import org.jetbrains.annotations.Nullable;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:37 PM
 */
public interface ApplicationModel {

    void addListener(ApplicationModelListener mock);

    void setStatus(@Nullable String status);

    void fireExceptionEvent(Throwable e);

    ItemsTableModel getItemsTableModel();

    void fireCheckServerDone();

    void publishLogStatus(LogEvent event);

    void fireLoggedInEvent(Account account);

    void fireLoginFailedEvent(Account account);

    void login(String login, String password, boolean savePassword);

    ComboBoxModel getUserNamesModel();

    Account getAccount(String selectedItem);

    void fireConfigurationChanged();
}
