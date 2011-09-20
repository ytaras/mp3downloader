package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.mostlymusic.downloader.DownloaderModule;
import org.junit.Before;
import org.junit.Test;

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
    public void shouldConvertFromUnderscoreNames() throws Exception {
        // given
        OrdersMetadataDto ordersMetadataDto = new OrdersMetadataDto(1, 2);
        // when
        String s = gson.toJson(ordersMetadataDto);
        // then
        assertThat(s).contains("last_item_id").contains("total_items");
    }
}
