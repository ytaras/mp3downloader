package com.mostlymusic.downloader.gui;

import com.google.inject.Singleton;
import com.mostlymusic.downloader.client.AuthService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.gui.worker.CheckServerUpdatesWorkerFactory;
import com.mostlymusic.downloader.gui.worker.LoginWorker;
import com.mostlymusic.downloader.localdata.AccountMapper;
import com.mostlymusic.downloader.manager.AccountManager;

import javax.inject.Inject;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:38 PM
 */
@Singleton
public class DefaultApplicationModel implements ApplicationModel {

    private final AuthService authService;
    private final List<ApplicationModelListener> listeners = new LinkedList<ApplicationModelListener>();
    private final ItemsTableModel itemsTableModel;
    private static final String LOGGED_IN_FORMAT = "Logged in as '%s'";

    @Inject
    public DefaultApplicationModel(AccountMapper accountMapper, AuthService authService,
                                   final CheckServerUpdatesWorkerFactory workerFactory,
                                   final AccountManager accountManager, final ItemsTableModel itemsTableModel) {
        this.accountMapper = accountMapper;
        this.authService = authService;
        this.itemsTableModel = itemsTableModel;
        addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(final Account account) {
                publishLogStatus(new LogEvent(String.format(LOGGED_IN_FORMAT, account.getUsername())));
                accountManager.setCurrentAccount(account);
                DefaultApplicationModel.this.accountMapper.setLastLoggedIn(account.getUsername());
                workerFactory.schedule();
            }

            @Override
            public void checkServerDone() {
                itemsTableModel.fireTableDataChanged();
            }

            @Override
            public void configurationChanged() {
                workerFactory.schedule();
            }
        });
    }

    private final AccountMapper accountMapper;

    @Override
    public ItemsTableModel getItemsTableModel() {
        return itemsTableModel;
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
            listener.loginFailed();
        }
    }

    @Override
    public void login(String login, String password, boolean savePassword) {
        Account account = accountMapper.findByLoginName(login);
        if (null == account) {
            accountMapper.createAccount(new Account(login));
            account = accountMapper.findByLoginName(login);
        }
        if (savePassword) {
            account.setPassword(password);
        } else {
            account.setPassword(null);
        }
        this.setStatus("Trying to log in...");
        new LoginWorker(this, account, authService, password).execute();
    }

    @Override
    public ComboBoxModel getUserNamesModel() {
        List<String> strings = accountMapper.listLoginNames("");
        return new DefaultComboBoxModel(new Vector<String>(strings));
    }

    @Override
    public Account getAccount(String selectedItem) {
        return accountMapper.findByLoginName(selectedItem);
    }

    @Override
    public void fireConfigurationChanged() {
        for (ApplicationModelListener listener : listeners) {
            listener.configurationChanged();
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
