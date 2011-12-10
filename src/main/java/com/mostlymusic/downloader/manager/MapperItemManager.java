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
    private List<ItemMapperListener> listeners = new LinkedList<ItemMapperListener>();

    public MapperItemManager(ItemMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void addListener(ItemMapperListener itemMapperListener) {
        listeners.add(itemMapperListener);
    }

    @Override
    public void saveItem(Item item, Account account) {
        if (mapper.contains(item.getItemId())) {
            mapper.updateItem(item, account);
            fireItemUpdated(item, account);
        } else {
            mapper.insertItem(item, account);
            fireItemAdded(item, account);
        }
    }

    private void fireItemUpdated(Item item, Account account) {
        for (ItemMapperListener listener : listeners) {
            listener.updatedItem(item, account);
        }
    }

    private void fireItemAdded(Item item, Account account) {
        for (ItemMapperListener listener : listeners) {
            listener.addedItem(item, account);
        }
    }
}
