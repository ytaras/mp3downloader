package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;

import java.io.IOException;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 1:47 PM
 */
public interface ProductsService {
    String ID_PARAM_NAME = "product_ids";

    List<Product> getProducts(List<Long> ids) throws IOException, RequestException;
}
