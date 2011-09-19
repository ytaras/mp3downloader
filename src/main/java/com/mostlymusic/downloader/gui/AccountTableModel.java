package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.localdata.AccountMapper;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
        addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                refresh();
            }
        });
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
        return getAccountAt(row).getUsername();
    }

    public void deleteAccountAt(int selectedRow) {
        accountMapper.deleteAccount(getAccountAt(selectedRow).getId());
        fireTableDataChanged();
    }

    public Account getAccountAt(int row) {
        return getData().get(row);
    }
}
