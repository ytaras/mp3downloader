package com.mostlymusic.downloader.client;

import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
public interface IOrdersService {
    /**
     * @return Orders list metadata like last order id and count of available orders
     */
    OrdersMetadataDto getOrdersMetadata() throws IOException;
}
