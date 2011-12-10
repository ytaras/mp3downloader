package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.localdata.ItemMapper;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 *
 * @author ytaras
 */
public class ItemManagerTest {

    private ItemMapper mapper;
    private ItemManager manager;
    private ItemMapperListener listener;
    private Item item;
    private Account account;
    private AccountManager accountManager;

    @Before
    public void setUp() throws Exception {
        mapper = mock(ItemMapper.class);
        accountManager = new MapperAccountManager();
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

    @Test
    public void shouldFindItemByStatus() {
        // given
        List<Item> expected = new LinkedList<Item>();
        when(mapper.findItemsByStatus(account, Item.AVAILABLE)).thenReturn(expected);

        // when
        List<Item> result = manager.findItemByStatus(Item.AVAILABLE);

        // then
        assertThat(result).isSameAs(expected);
    }

    @Test
    public void shouldReturnNoItemsIfLoggedOut() {
        // given
        accountManager.setCurrentAccount(null);

        // when
        List<Item> items = manager.findItem();

        // then
        assertThat(items).isEmpty();
        verifyZeroInteractions(mapper);
    }

    @Test
    public void shouldReturnAllItems() {
        // given
        when(mapper.listItems(account)).thenReturn(Collections.singletonList(item));
        // when
        List<Item> items = manager.findItem();

        // then
        assertThat(items).containsOnly(item);

    }
}
