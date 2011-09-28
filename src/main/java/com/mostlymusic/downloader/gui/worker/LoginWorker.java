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
    private final AuthService authService;
    private final String password;

    public LoginWorker(ApplicationModel applicationModel, Account account, AuthService authService, String password) {
        super(applicationModel);
        this.account = account;
        this.authService = authService;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        return authService.auth(account.getUsername(), password);
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
