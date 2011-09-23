package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;
import com.mostlymusic.downloader.AuthService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.gui.worker.CheckServerUpdatesWorker;
import com.mostlymusic.downloader.gui.worker.LoginWorker;
import com.mostlymusic.downloader.localdata.AccountMapper;
import com.mostlymusic.downloader.localdata.ItemsMapper;

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
    private List<ApplicationModelListener> listeners = new LinkedList<ApplicationModelListener>();
    private ItemsTableModel itemsTableModel;
    private Account loggedAccount;
    private static final String LOGGED_IN_FORMAT = "Logged in as '%s'";

    @Inject
    public DefaultApplicationModel(AccountMapper accountMapper, AuthService authService,
                                   final CheckServerUpdatesWorker worker, ItemsMapper itemsMapper) {
        this.accountMapper = accountMapper;
        this.authService = authService;
        accountTableModel = new AccountTableModel(accountMapper);
        itemsTableModel = new ItemsTableModel(this, itemsMapper);
        addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(final Account account) {
                publishLogStatus(new LogEvent(String.format(LOGGED_IN_FORMAT, account.getUsername())));
                loggedAccount = account;
                worker.setAccount(account);
                worker.execute();
            }

            @Override
            public void checkServerDone() {
                itemsTableModel.fireTableDataChanged();
            }
        });
    }

    private final AccountMapper accountMapper;

    @Override
    public AccountTableModel getAccountsTableModel() {
        return accountTableModel;
    }

    @Override
    public ItemsTableModel getItemsTableModel() {
        return itemsTableModel;
    }

    @Override
    public Account getLoggedAccount() {
        return loggedAccount;
    }

    @Override
    public void fireCheckServerDone() {
        for (ApplicationModelListener listener : listeners) {
            listener.checkServerDone();
        }
    }

    @Override
    public void publishLogStatus(LogEvent event) {
        if (null == event) {
            return;
        }
        for (ApplicationModelListener listener : listeners) {
            listener.logEvent(event);
        }
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
    public void fireExceptionEvent(Throwable e) {
        for (ApplicationModelListener listener : listeners) {
            listener.exceptionOccurred(e);
        }
    }

    @Override
    public void fireLoginFailedEvent(Account account) {
        for (ApplicationModelListener listener : listeners) {
            listener.loginFailed(account);
        }
    }

    @Override
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
