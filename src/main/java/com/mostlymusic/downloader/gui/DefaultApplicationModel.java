package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.AuthService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.localdata.AccountMapper;

import javax.inject.Inject;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:38 PM
 */
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
        Account account = accountTableModel.getAccountAt(selectedRow);
        this.setStatus("Trying to log in...");
        try {
            authService.auth(account.getUsername(), account.getPassword());
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to login", e);
        } finally {
            this.setStatus(null);
        }
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
