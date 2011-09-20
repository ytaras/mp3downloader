package com.mostlymusic.downloader.client;

import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.ServiceUrl;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
@Singleton
public class OrdersService extends JsonServiceClient implements IOrdersService {
    private final String serviceUrl;
    private static final Type TYPE = new TypeToken<List<TrackDto>>() {
    }.getType();

    @Inject
    public OrdersService(@ServiceUrl String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public List<TrackDto> getTracks(long lastOrderId, int page, int pageSize) throws IOException {
        List<NameValuePair> pairs = new LinkedList<NameValuePair>();
        pairs.add(new BasicNameValuePair(LAST_ORDER_ID_PARAM_NAME, "" + lastOrderId));
        pairs.add(new BasicNameValuePair(PAGE_PARAM_NAME, "" + page));
        pairs.add(new BasicNameValuePair(PAGE_SIZE_PARAM_NAME, "" + pageSize));
        HttpPost post = new HttpPost(serviceUrl + "/itemsList/");
        post.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
        return getResult(post, TYPE);
    }

    @Override
    public OrdersMetadataDto getOrdersMetadata() throws IOException {
        HttpPost get = new HttpPost(serviceUrl + "/itemsStatus/");
        return getResult(get, OrdersMetadataDto.class);
    }

    @Override
    public OrdersMetadataDto getOrdersMetadata(long lastOrderId) throws IOException {
        HttpPost get = new HttpPost(serviceUrl + "/itemsStatus/");
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(Collections.singletonList(
                new BasicNameValuePair(LAST_ORDER_ID_PARAM_NAME, "" + lastOrderId)));
        get.setEntity(formEntity);
        return getResult(get, OrdersMetadataDto.class);
    }

}
