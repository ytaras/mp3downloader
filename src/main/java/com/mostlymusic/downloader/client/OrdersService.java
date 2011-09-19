package com.mostlymusic.downloader.client;

import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
public class OrdersService extends JsonServiceClient implements IOrdersService {
    private String serviceUrl;
    private static final Type TYPE = new TypeToken<List<TrackDto>>() {
    }.getType();

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public List<TrackDto> getTracks() throws IOException {
        HttpGet get = new HttpGet(serviceUrl + "/list");
        return getResult(get, TYPE);
    }

    @Override
    public OrdersMetadataDto getOrdersMetadata() throws IOException {
        HttpGet get = new HttpGet(serviceUrl);
        return getResult(get, OrdersMetadataDto.class);
    }

    @Override
    public OrdersMetadataDto getOrdersMetadata(long lastOrderId) throws IOException {
        HttpGet get = new HttpGet(serviceUrl + "?" + LAST_ORDER_ID_PARAM_NAME + "=" + lastOrderId);
        return getResult(get, OrdersMetadataDto.class);
    }

}
