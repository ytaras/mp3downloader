package com.mostlymusic.downloader.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ytaras
 */
@Singleton
public class MapperItemManager implements ItemManager {
    private final ItemMapper mapper;
    private final AccountManager accountManager;
    private List<ItemMapperListener> listeners = new LinkedList<ItemMapperListener>();

    @Inject
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

    @Override
    public List<Item> findItemByStatus(String status) {
        return mapper.findItemsByStatus(accountManager.getCurrentAccount(), status);
    }

    @Override
    public List<Item> findItem() {
        Account currentAccount = accountManager.getCurrentAccount();
        if (null == currentAccount) {
            return Collections.emptyList();
        } else {
            return mapper.listItems(currentAccount);
        }
    }

    @Override
    public void setStatus(long itemId, String status) {
        mapper.setStatus(itemId, status);
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
