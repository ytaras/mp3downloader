package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;
import com.mostlymusic.downloader.AuthService;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import com.mostlymusic.downloader.gui.worker.CheckServerUpdatesWorker;
import com.mostlymusic.downloader.gui.worker.LoginWorker;
import com.mostlymusic.downloader.localdata.AccountMapper;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:38 PM
 */
@Singleton
public class DefaultApplicationModel implements ApplicationModel {

    private AccountTableModel accountTableModel;
    private AuthService authService;
    private ItemsService itemsService;
    private List<ApplicationModelListener> listeners = new LinkedList<ApplicationModelListener>();

    @Inject
    public DefaultApplicationModel(AccountMapper accountMapper, AuthService authService, final ItemsService itemsService) {
        this.accountMapper = accountMapper;
        this.authService = authService;
        this.itemsService = itemsService;
        accountTableModel = new AccountTableModel(accountMapper);
        final ItemsService itemsService1 = itemsService;
        addListener(new ApplicationModelListenerAdapter() {
            private final ItemsService itemsService = itemsService1;

            @Override
            public void loggedIn(final Account account) {
                setStatus("Fetching list of updates from server...");
                new CheckServerUpdatesWorker(account, itemsService, DefaultApplicationModel.this).execute();
            }

            @Override
            public void metadataFetched(ItemsMetadataDto itemsMetadataDto, Account account) {
                setStatus(null);
            }
        });
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
        new LoginWorker(this, account, authService).execute();
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

    @Override
    public void fireMetadataFetchedEvent(ItemsMetadataDto itemsMetadataDto, Account account) {
        for (ApplicationModelListener listener : listeners) {
            listener.metadataFetched(itemsMetadataDto, account);
        }
    }

    @Override
    public void fireExceptionEvent(Exception e) {
        for (ApplicationModelListener listener : listeners) {
            listener.exceptionOccurred(e);
        }
    }

    public void fireLoginFailedEvent(Account account) {
        for (ApplicationModelListener listener : listeners) {
            listener.loginFailed(account);
        }
    }

    public void fireLoggedInEvent(Account account) {
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
