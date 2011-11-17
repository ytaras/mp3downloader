package com.mostlymusic.downloader.localdata;

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
    private ItemMapper itemMapper;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        itemMapper = injector.getInstance(ItemMapper.class);
        dataSource = injector.getInstance(DataSource.class);
        injector.getInstance(SchemaCreator.class).createTables();
    }

    @Test
    public void shouldInitMyBatis() {
        // given
        // when
        List<Item> items = itemMapper.listLinks(new Account());

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
        itemMapper.insertItem(item, account);

        // then
        List<Item> items = itemMapper.listLinks(account);
        assertThat(items).contains(item);
    }

    private Item getMockItem(Account account) {
        Item item = new Item();
        setProperties(account, item, 0);
        return item;
    }

    private void setProperties(Account account, Item item, Integer suffix) {
        item.setCreatedAt(new Date());
        item.setUpdatedAt(new Date());
        item.setDownloadsBought(4 + account.getId() + suffix);
        item.setDownloadsUsed(3 + account.getId() + suffix);
        item.setFileName("FileName" + suffix);
        item.setItemId(123 + account.getId() + suffix);
        item.setLinkId(321 + account.getId() + suffix);
        item.setLinkHash("LHASH" + account.getId() + suffix);
        item.setLinkTitle("LTITLE" + account.getId() + suffix);
        item.setStatus("STATUS" + account.getId() + suffix);
        item.setProductId(444 + account.getId() + suffix);
        item.setProductName("ProductName" + suffix);
        item.setParentProductId(1L + suffix);
        item.setMainArtistId(123 + suffix);
        item.setDirty(true);
    }

    @Test
    public void shouldFilterByAccount() {
        // given
        Account account = new Account();
        account.setId(1);
        Item mockItem = getMockItem(account);
        itemMapper.insertItem(mockItem, account);
        Account account2 = new Account();
        account2.setId(2);
        itemMapper.insertItem(getMockItem(account2), account2);

        // when
        List<Item> items = itemMapper.listLinks(account);

        // then
        assertThat(items).containsExactly(mockItem);
    }

    @Test
    public void shouldUpgrade() {
        // given
        Account account = new Account();
        account.setId(1);
        Item mockItem = getMockItem(account);
        itemMapper.insertItem(mockItem, account);
        long savedId = mockItem.getItemId();
        setProperties(account, mockItem, 2);
        mockItem.setItemId(savedId);

        // when
        itemMapper.updateItem(mockItem, account);

        // then
        assertThat(itemMapper.listLinks(account)).containsExactly(mockItem);
    }

    @Test
    public void shouldDetectIfContains() {
        // given
        Account account = new Account();
        account.setId(1);
        Item mockItem = getMockItem(account);


        // when
        itemMapper.insertItem(mockItem, account);

        // then
        assertThat(itemMapper.contains(mockItem.getItemId() + 1)).isFalse();
        assertThat(itemMapper.contains(mockItem.getItemId())).isTrue();
    }

    @Test
    public void shouldSetStatus() {
        // given
        Account account = new Account();
        account.setId(1);
        Item mockItem = getMockItem(account);
        itemMapper.insertItem(mockItem, account);

        // when
        itemMapper.setStatus(mockItem.getItemId(), "AAA");

        // then
        Item loaded = itemMapper.listLinks(account).get(0);
        assertThat(loaded.getStatus()).isEqualTo("AAA");
    }

    @After
    public void cleanTable() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            connection.prepareStatement("DELETE FROM " + ItemMapper.TABLE_NAME).executeUpdate();
        } finally {
            connection.close();
        }
    }
}
