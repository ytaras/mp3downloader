package com.mostlymusic.downloader.client;

import java.io.IOException;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
public interface IOrdersService {
    String LAST_ORDER_ID_PARAM_NAME = "first_item_id";
    String PAGE_PARAM_NAME = "page";
    String PAGE_SIZE_PARAM_NAME = "pageSize";
    String ID_PARAM_NAME = "ID";

    /**
     * @return Orders list metadata like last order id and count of available orders
     */
    OrdersMetadataDto getOrdersMetadata() throws IOException;

    /**
     * @param lastOrderId Last order id that we know about
     * @return Orders list metadata like last order id and count of available orders after supplied last order
     */
    OrdersMetadataDto getOrdersMetadata(long lastOrderId) throws IOException;

    /**
     * @param lastOrderId Last order id we know about
     * @param page        page number
     * @param pageSize    page size
     * @return list of tracks on this page
     */
    List<TrackDto> getTracks(long lastOrderId, int page, int pageSize) throws IOException;
}
