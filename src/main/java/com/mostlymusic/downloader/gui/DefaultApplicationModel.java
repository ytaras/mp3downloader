package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.AuthService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.localdata.AccountMapper;

import javax.inject.Inject;
import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:38 PM
 */
public class DefaultApplicationModel implements ApplicationModel {

    private AccountTableModel accountTableModel;
    private AuthService authService;

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
        try {
            authService.auth(account.getUsername(), account.getPassword());
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to login", e);
        }
    }

}
