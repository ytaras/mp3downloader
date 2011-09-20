package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;
import com.mostlymusic.downloader.AuthService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.localdata.AccountMapper;

import javax.inject.Inject;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:38 PM
 */
@Singleton
public class DefaultApplicationModel implements ApplicationModel {

    private AccountTableModel accountTableModel;
    private AuthService authService;
    private List<ApplicationModelListener> listeners = new LinkedList<ApplicationModelListener>();

    @Inject
    public DefaultApplicationModel(AccountMapper accountMapper, AuthService authService) {
        this.accountMapper = accountMapper;
        this.authService = authService;
        accountTableModel = new AccountTableModel(accountMapper);
    }

    private final AccountMapper accountMapper;

    @Override
    public AccountTableModel getAccountsTableModel() {
        return accountTableModel;
    }

    @Override
    public void createAccount(String username, char[] password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(new String(password));
        accountMapper.createAccount(account);
        accountTableModel.fireTableDataChanged();
    }

    @Override
    public void loginToAccountAt(int selectedRow) {
        final Account account = accountTableModel.getAccountAt(selectedRow);
        this.setStatus("Trying to log in...");
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return authService.auth(account.getUsername(), account.getPassword());
            }

            @Override
            protected void done() {
                setStatus(null);
                try {
                    if (get()) {
                        fireLoggedInEvent(account);
                    } else {
                        fireLoginFailedEvent(account);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    @Override
    public void addListener(ApplicationModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public void setStatus(String status) {
        if (null == status || status.isEmpty()) {
            fireStatusUnsetEvent();
        } else {
            fireStatusSetEvent(status);
        }

    }

    private void fireLoginFailedEvent(Account account) {
        for (ApplicationModelListener listener : listeners) {
            listener.loginFailed(account);
        }
    }

    private void fireLoggedInEvent(Account account) {
        for (ApplicationModelListener listener : listeners) {
            listener.loggedIn(account);
        }
    }

    private void fireStatusSetEvent(String status) {
        for (ApplicationModelListener listener : listeners) {
            listener.statusSet(status);
        }
    }

    private void fireStatusUnsetEvent() {
        for (ApplicationModelListener listener : listeners) {
            listener.statusUnset();
        }
    }
}
