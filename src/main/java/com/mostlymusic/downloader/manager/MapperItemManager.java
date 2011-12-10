package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.localdata.ItemMapper;

import java.util.LinkedList;
import java.util.List;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public class MapperItemManager implements ItemManager {
    private final ItemMapper mapper;
    private final AccountManager accountManager;
    private List<ItemMapperListener> listeners = new LinkedList<ItemMapperListener>();

    public MapperItemManager(ItemMapper mapper, AccountManager accountManager) {
        this.mapper = mapper;
        this.accountManager = accountManager;
    }

    @Override
    public void addListener(ItemMapperListener itemMapperListener) {
        listeners.add(itemMapperListener);
    }

    @Override
    public void saveItem(Item item) {
        Account account = accountManager.getCurrentAccount();
        if (mapper.contains(item.getItemId())) {
            mapper.updateItem(item, account);
            fireItemUpdated(item);
        } else {
            mapper.insertItem(item, account);
            fireItemAdded(item, account);
        }
    }

    private void fireItemUpdated(Item item) {
        for (ItemMapperListener listener : listeners) {
            listener.updatedItem(item);
        }
    }

    private void fireItemAdded(Item item, Account account) {
        for (ItemMapperListener listener : listeners) {
            listener.addedItem(item, account);
        }
    }
}
