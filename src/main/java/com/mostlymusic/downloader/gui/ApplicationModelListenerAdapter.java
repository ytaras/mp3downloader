package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 1:29 PM
 */
public class ApplicationModelListenerAdapter implements ApplicationModelListener {
    @Override
    public void statusUnset() {
    }

    @Override
    public void statusSet(String status) {
    }

    @Override
    public void loginFailed() {
    }

    @Override
    public void loggedIn(Account account) {
    }

    @Override
    public void exceptionOccurred(Throwable e) {
    }

    @Override
    public void checkServerDone() {
    }

    @Override
    public void logEvent(LogEvent event) {
    }

    @Override
    public void configurationChanged() {
    }
}
