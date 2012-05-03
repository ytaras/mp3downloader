package com.mostlymusic.downloader.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.mostlymusic.downloader.client.exceptions.RequestException;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.dto.ItemsDto;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import org.jetbrains.annotations.Nullable;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
public interface ItemsService {
    String LAST_ITEM_ID_PARAM_NAME = "last_item_id";
    String FIRST_ITEM_ID_PARAM_NAME = "first_item_id";
    String PAGE_PARAM_NAME = "page_num";
    String PAGE_SIZE_PARAM_NAME = "page_size";
    String ID_PARAM_NAME = "ID";

    /**
     * @param lastOrderId Last order id that we know about
     * @return Orders list metadata like last order id and count of available orders after supplied last order
     */
    ItemsMetadataDto getOrdersMetadata(@Nullable Long lastOrderId) throws IOException, RequestException;


    /**
     * @param lastOrderId Last order id we know about
     * @param page        page number
     * @param pageSize    page size
     * @return list of tracks on this page
     */
    ItemsDto getTracks(@Nullable Long firstOrderId, long lastOrderId, int page, int pageSize) throws IOException, RequestException;

    Map.Entry<InputStream, Long> getTrack(Item link) throws IOException, RequestException;
}
