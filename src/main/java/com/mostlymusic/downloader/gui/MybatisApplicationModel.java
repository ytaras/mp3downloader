package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.localdata.AccountMapper;

import javax.inject.Inject;
import javax.swing.table.TableModel;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:38 PM
 */
public class MybatisApplicationModel implements ApplicationModel {
    @Inject
    public MybatisApplicationModel(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    private final AccountMapper accountMapper;

    @Override
    public TableModel getAccountsTableModel() {
        return new AccountTableModel(accountMapper);
    }

}
