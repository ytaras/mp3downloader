package com.mostlymusic.downloader.client;

import com.google.gson.reflect.TypeToken;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.lang.reflect.Type;
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

    public ProductsService(String serviceUrl) {
        this.serviceUrl = serviceUrl + "/products";
    }

    @Override
    public List<ProductDto> getProducts(int... ids) throws IOException {
        StringBuilder productIds = new StringBuilder();
        for (int id : ids) {
            productIds.append(id).append(',');
        }
        productIds.deleteCharAt(productIds.length() - 1);
        HttpGet httpGet = new HttpGet(serviceUrl + "?" +
                ID_PARAM_NAME + "=" + productIds.toString());
        return getResult(httpGet, PRODUCTS_TYPE);
    }
}
