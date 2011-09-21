package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.dto.ItemsDto;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 2:39 PM
 */
public class GsonTest {

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = Guice.createInjector(new DownloaderModule("")).getInstance(Gson.class);
    }

    @Test
    public void orderMetadataShouldHaveCorrectNaming() throws Exception {
        // given
        ItemsMetadataDto itemsMetadataDto = new ItemsMetadataDto(1, 2);
        // when
        String s = gson.toJson(itemsMetadataDto);
        // then
        assertThat(s).contains("last_item_id").contains("total_items");
    }

    @Test
    public void itemShouldHaveCorrectNaming() throws Exception {
        // given
        Item item = new Item();
        item.setLinkHash("lh");
        item.setLinkTitle("lt");
        item.setStatus("st");
        item.setFileName("fn");
        item.setCreatedAt(new Date());
        item.setUpdatedAt(new Date());

        // when
        String s = gson.toJson(item);

        // then
        assertThat(s).contains("item_id").contains("product_id").contains("link_hash").contains("downloads_bought");
        assertThat(s).contains("downloads_used").contains("link_id").contains("link_title").contains("status");
        assertThat(s).contains("file_name").contains("created_at").contains("updated_at");
    }

    @Test
    public void itemsShouldHaveCorrectNaming() throws Exception {
        // given
        ItemsDto itemsDto = new ItemsDto();

        // when
        String s = gson.toJson(itemsDto);

        // then
        assertThat(s).contains("info").contains("page_current").contains("page_total").contains("page_size").contains("total_records");
        assertThat(s).contains("items");
    }

    @Test
    public void shoulSerializeDeserialize() throws Exception {
        // given
        ItemsMetadataDto itemsMetadataDto = new ItemsMetadataDto(123, 345);
        // when
        assertThat(gson.fromJson(gson.toJson(itemsMetadataDto), ItemsMetadataDto.class)).isEqualTo(itemsMetadataDto);
        // then
    }

    @Test
    public void shouldParseDate() {
        // given
        String serialized = "{created_at: \"2011-09-21 07:17:28\"}";

        // when
        Item deserialized = gson.fromJson(serialized, Item.class);

        // then
        assertThat(deserialized.getCreatedAt()).isNotNull();

    }
}
