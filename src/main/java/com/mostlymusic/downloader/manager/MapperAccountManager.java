package com.mostlymusic.downloader.manager;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;

/**
 *
 * @author ytaras
 */
@Singleton
public class MapperAccountManager implements AccountManager {
    private List<AccountManagerListener> listeners = new LinkedList<AccountManagerListener>();
    private Account currentAccount;

    @Override
    public void addListener(AccountManagerListener listener) {
        listeners.add(listener);
    }

    @Override
    public void setCurrentAccount(Account account) {
        currentAccount = account;
        fireAccountChanged(account);
    }

    @Override
    public Account getCurrentAccount() {
        return currentAccount;
    }

    private void fireAccountChanged(Account account) {
        for (AccountManagerListener listener : listeners) {
            listener.currentAccountChanged(account);
        }
    }
}
