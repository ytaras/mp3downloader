package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.localdata.AccountMapper;

import javax.inject.Inject;
import javax.swing.table.TableModel;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:38 PM
 */
public class MybatisApplicationModel implements ApplicationModel {

    private AccountTableModel accountTableModel;

    @Inject
    public MybatisApplicationModel(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
        accountTableModel = new AccountTableModel(accountMapper);
    }

    private final AccountMapper accountMapper;

    @Override
    public TableModel getAccountsTableModel() {

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

}
