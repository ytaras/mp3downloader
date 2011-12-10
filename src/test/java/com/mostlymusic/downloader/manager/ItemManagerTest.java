package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.localdata.ItemMapper;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public class ItemManagerTest {

    private ItemMapper mapper;
    private ItemManager manager;
    private ItemMapperListener listener;
    private Item item;
    private Account account;

    @Before
    public void setUp() throws Exception {
        mapper = mock(ItemMapper.class);
        AccountManager accountManager = new MapperAccountManager();
        manager = new MapperItemManager(mapper, accountManager);
        listener = mock(ItemMapperListener.class);
        manager.addListener(listener);
        item = new Item();
        item.setItemId(12342);
        account = new Account();
        accountManager.setCurrentAccount(account);
    }

    @Test
    public void shouldUpdateItemIfExists() {
        // given
        when(mapper.contains(item.getItemId())).thenReturn(false);

        // when
        manager.saveItem(item);

        // then
        verify(mapper).insertItem(item, account);
        verify(listener).addedItem(item, account);
    }

    @Test
    public void shouldInsertItemIfNew() {
        // given
        when(mapper.contains(item.getItemId())).thenReturn(true);

        // when
        manager.saveItem(item);

        // then
        verify(mapper).updateItem(item, account);
        verify(listener).updatedItem(item);
    }
}
