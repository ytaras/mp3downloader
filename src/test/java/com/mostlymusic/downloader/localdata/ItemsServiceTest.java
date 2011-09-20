package com.mostlymusic.downloader.localdata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.LocalStorageModule;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.ItemDto;
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
public class ItemsServiceTest extends StoragetTestBase {
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
        List<ItemDto> items = itemsMapper.listLinks(new Account());

        // then
        assertThat(items).isNotNull();
    }

    @Test
    public void shouldInsert() {
        // given
        Account account = new Account();
        account.setId(1);
        ItemDto itemDto = new ItemDto();
        itemDto.setCreatedAt(new Date());
        itemDto.setUpdatedAt(new Date());
        itemDto.setDownloadsBought(4);
        itemDto.setDownloadsUsed(3);
        itemDto.setFileName("FileName");
        itemDto.setItemId(123);
        itemDto.setLinkId(321);
        itemDto.setLinkHash("LHASH");
        itemDto.setLinkTitle("LTITLE");
        itemDto.setStatus("STATUS");
        itemDto.setProductId(444);

        // when
        itemsMapper.insertItem(itemDto, account);

        // then
        List<ItemDto> itemDtos = itemsMapper.listLinks(account);
        assertThat(itemDtos).contains(itemDto);
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
