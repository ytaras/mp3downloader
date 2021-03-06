package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 1:29 PM
 */
public interface ApplicationModelListener {
    void statusUnset();

    void statusSet(String status);

    void loginFailed();

    void loggedIn(Account account);

    void exceptionOccurred(Throwable e);

    void checkServerDone();

    void logEvent(LogEvent event);

    void configurationChanged();
}
