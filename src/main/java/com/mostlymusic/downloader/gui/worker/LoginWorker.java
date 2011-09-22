package com.mostlymusic.downloader.gui.worker;

import com.mostlymusic.downloader.AuthService;
import com.mostlymusic.downloader.client.IAuthService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class LoginWorker extends SwingWorker<Boolean, Void> {
    private static final String LOGGED_IN_FORMAT = "Logged in as '%s'";
    private final Account account;
    private ApplicationModel applicationModel;
    private IAuthService authService;

    public LoginWorker(ApplicationModel applicationModel, Account account, AuthService authService) {
        this.applicationModel = applicationModel;
        this.account = account;
        this.authService = authService;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return authService.auth(account.getUsername(), account.getPassword());
    }

    @Override
    protected void done() {
        applicationModel.setStatus(null);
        applicationModel.publishLogStatus(new LogEvent(String.format(LOGGED_IN_FORMAT, account.getUsername())));
        try {
            if (get()) {
                applicationModel.fireLoggedInEvent(account);
            } else {
                applicationModel.fireLoginFailedEvent(account);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException("Error while trying to login", e);
        }
    }
}
