package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.localdata.ItemsMapper;

import javax.swing.table.AbstractTableModel;
import java.util.Collections;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/21/11
 *         Time: 10:41 AM
 */
public class ItemsTableModel extends AbstractTableModel {
    private ApplicationModel applicationModel;
    private ItemsMapper itemsMapper;

    public ItemsTableModel(ApplicationModel applicationModel, ItemsMapper itemsMapper) {
        this.applicationModel = applicationModel;
        this.itemsMapper = itemsMapper;
    }

    @Override
    public int getRowCount() {
        return getData().size();
    }

    private List<Item> getData() {
        // TODO Caching
        Account loggedAccount = applicationModel.getLoggedAccount();
        if (null == loggedAccount) {
            return Collections.emptyList();
        } else {
            return itemsMapper.listLinks(loggedAccount);
        }

    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int row, int col) {
        return getData().get(row).getLinkTitle();
    }
}
