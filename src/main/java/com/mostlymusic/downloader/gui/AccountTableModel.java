package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.localdata.AccountMapper;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:47 PM
 */
class AccountTableModel extends AbstractTableModel {
    private AccountMapper accountMapper;

    AccountTableModel(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    private List<Account> data;

    public synchronized List<Account> getData() {
        if (null == data) {
            refresh();
        }
        return data;
    }

    private void refresh() {
        data = accountMapper.listAccounts();
    }

    @Override
    public int getRowCount() {

        return getData().size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return getData().get(row).getUsername();
    }
}
