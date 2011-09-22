package com.mostlymusic.downloader.client;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.ServiceUrl;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.dto.ItemsDto;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
@Singleton
public class ItemsService extends JsonServiceClient implements IItemsService {
    private final String serviceUrl;

    @Inject
    public ItemsService(@ServiceUrl String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public ItemsDto getTracks(Long firstOrderId, long lastOrderId, int page, int pageSize) throws IOException {
        LinkedList<NameValuePair> params = new LinkedList<NameValuePair>();
        if (null != firstOrderId) {
            params.add(new BasicNameValuePair(FIRST_ITEM_ID_PARAM_NAME, "" + firstOrderId));
        }
        params.add(new BasicNameValuePair(LAST_ITEM_ID_PARAM_NAME, "" + lastOrderId));
        params.add(new BasicNameValuePair(PAGE_PARAM_NAME, "" + page));
        params.add(new BasicNameValuePair(PAGE_SIZE_PARAM_NAME, "" + pageSize));
        HttpPost post = new HttpPost(serviceUrl + "/download-manager/sync/itemsList/");
        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        return getResult(post, ItemsDto.class);
    }

    @Override
    public InputStream getTrack(Item link) throws IOException {
        HttpPost httpPost = new HttpPost(serviceUrl + "/download-manager/files/download/id/" + link.getLinkHash());
        HttpResponse response = getHttpClient().execute(httpPost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            return response.getEntity().getContent();
        }
        throw new HttpResponseException(response.getStatusLine().getStatusCode(),
                getEntityContent(response.getEntity()));
    }

    @Override
    public ItemsMetadataDto getOrdersMetadata(Long lastOrderId) throws IOException {
        HttpPost get = new HttpPost(serviceUrl + "/download-manager/sync/itemsStatus/");
        if (null != lastOrderId) {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(Collections.singletonList(
                    new BasicNameValuePair(FIRST_ITEM_ID_PARAM_NAME, "" + lastOrderId)));
            get.setEntity(formEntity);
        }
        return getResult(get, ItemsMetadataDto.class);
    }

}
