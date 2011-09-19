package com.mostlymusic.downloader.client;

import java.io.IOException;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
public interface IOrdersService {
    String LAST_ORDER_ID_PARAM_NAME = "lastOrderId";

    /**
     * @return Orders list metadata like last order id and count of available orders
     */
    OrdersMetadataDto getOrdersMetadata() throws IOException;

    /**
     *
     * @param lastOrderId Last order id that we know about
     * @return Orders list metadata like last order id and count of available orders after supplied last order
     */
    OrdersMetadataDto getOrdersMetadata(long lastOrderId) throws IOException;

    List<TrackDto> getTracks() throws IOException;
}
