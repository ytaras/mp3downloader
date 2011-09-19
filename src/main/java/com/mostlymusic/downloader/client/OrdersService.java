package com.mostlymusic.downloader.client;

import org.apache.http.client.methods.HttpGet;

import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
public class OrdersService extends JsonServiceClient implements IOrdersService {
    private String serviceUrl;

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public OrdersMetadataDto getOrdersMetadata() throws IOException {
        HttpGet get = new HttpGet(serviceUrl);
        return getResult(get, OrdersMetadataDto.class);
    }

}
