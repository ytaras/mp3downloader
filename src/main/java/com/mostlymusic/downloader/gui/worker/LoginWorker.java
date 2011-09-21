package com.mostlymusic.downloader.gui.worker;

import com.mostlymusic.downloader.AuthService;
import com.mostlymusic.downloader.client.IAuthService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.gui.DefaultApplicationModel;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class LoginWorker extends SwingWorker<Boolean, Void> {
    private final Account account;
    private DefaultApplicationModel defaultApplicationModel;
    private IAuthService authService;

    public LoginWorker(DefaultApplicationModel defaultApplicationModel, Account account, AuthService authService) {
        this.defaultApplicationModel = defaultApplicationModel;
        this.account = account;
        this.authService = authService;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return authService.auth(account.getUsername(), account.getPassword());
    }

    @Override
    protected void done() {
        defaultApplicationModel.setStatus(null);
        try {
            if (get()) {
                defaultApplicationModel.fireLoggedInEvent(account);
            } else {
                defaultApplicationModel.fireLoginFailedEvent(account);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw new RuntimeException("Error while trying to login", e);
        }
    }
}
