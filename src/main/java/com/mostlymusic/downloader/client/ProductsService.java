package com.mostlymusic.downloader.client;

import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.mostlymusic.downloader.ServiceUrl;
import com.mostlymusic.downloader.client.exceptions.RequestException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 1:47 PM
 */
public class ProductsService extends JsonServiceClient implements IProductsService {
    private final String serviceUrl;
    private static final Type PRODUCTS_TYPE = new TypeToken<List<ProductDto>>() {
    }.getType();

    @Inject
    public ProductsService(@ServiceUrl String serviceUrl) {
        if (serviceUrl.isEmpty()) {
            throw new RuntimeException("service url should not be null");
        }
        this.serviceUrl = serviceUrl + "/download-manager/sync/products";
    }

    @Override
    public List<ProductDto> getProducts(int... ids) throws IOException, RequestException {
        StringBuilder productIds = new StringBuilder();
        for (int id : ids) {
            productIds.append(id).append(',');
        }
        productIds.deleteCharAt(productIds.length() - 1);
        HttpPost post = new HttpPost(serviceUrl);
        post.setEntity(new UrlEncodedFormEntity(Collections.singletonList(new BasicNameValuePair(ID_PARAM_NAME, productIds.toString()))));
        return getResult(post, PRODUCTS_TYPE);
    }
}
