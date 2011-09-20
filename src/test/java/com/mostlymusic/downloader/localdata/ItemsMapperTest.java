package com.mostlymusic.downloader.localdata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.LocalStorageModule;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 4:59 PM
 */
public class ItemsMapperTest extends StoragetTestBase {
    private DataSource dataSource;
    private ItemsMapper itemsMapper;


    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new LocalStorageModule());
        itemsMapper = injector.getInstance(ItemsMapper.class);
        dataSource = injector.getInstance(DataSource.class);
        injector.getInstance(SchemaCreator.class).createTables();
    }

    @Test
    public void shouldInitMyBatis() {
        // given
        // when
        List<Item> items = itemsMapper.listLinks(new Account());

        // then
        assertThat(items).isNotNull();
    }

    @Test
    public void shouldInsert() {
        // given
        Account account = new Account();
        account.setId(1);
        Item item = getMockItem(account);

        // when
        itemsMapper.insertItem(item, account);

        // then
        List<Item> items = itemsMapper.listLinks(account);
        assertThat(items).contains(item);
    }

    private Item getMockItem(Account account) {
        Item item = new Item();
        item.setCreatedAt(new Date());
        item.setUpdatedAt(new Date());
        item.setDownloadsBought(4 + account.getId());
        item.setDownloadsUsed(3 + account.getId());
        item.setFileName("FileName");
        item.setItemId(123 + account.getId());
        item.setLinkId(321 + account.getId());
        item.setLinkHash("LHASH" + account.getId());
        item.setLinkTitle("LTITLE" + account.getId());
        item.setStatus("STATUS" + account.getId());
        item.setProductId(444 + account.getId());
        return item;
    }

    @Test
    public void shouldFilterByAccount() {
        // given
        Account account = new Account();
        account.setId(1);
        Item mockItem = getMockItem(account);
        itemsMapper.insertItem(mockItem, account);
        Account account2 = new Account();
        account2.setId(2);
        itemsMapper.insertItem(getMockItem(account2), account2);

        // when
        List<Item> items = itemsMapper.listLinks(account);

        // then
        assertThat(items).containsExactly(mockItem);
    }

    @After
    public void cleanTable() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            connection.prepareStatement("DELETE FROM " + ItemsMapper.TABLE_NAME).executeUpdate();
        } finally {
            connection.close();
        }
    }
}
