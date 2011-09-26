package com.mostlymusic.downloader.gui.worker;

import com.mostlymusic.downloader.client.AuthService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.gui.ApplicationModel;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class LoginWorker extends AbstractSwingClientWorker<Boolean, Void> {
    private final Account account;
    private AuthService authService;

    public LoginWorker(ApplicationModel applicationModel, Account account, AuthService authService) {
        super(applicationModel);
        this.account = account;
        this.authService = authService;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return authService.auth(account.getUsername(), account.getPassword());
    }

    @Override
    protected void beforeGet() {
        getApplicationModel().setStatus(null);
    }

    @Override
    protected void doDone(Boolean loggedIn) {
        if (loggedIn) {
            getApplicationModel().fireLoggedInEvent(account);
        } else {
            getApplicationModel().fireLoginFailedEvent(account);
        }
    }
}
