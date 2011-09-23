package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;

import java.io.IOException;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 1:47 PM
 */
public interface IProductsService {
    String ID_PARAM_NAME = "id";

    List<ProductDto> getProducts(int... ids) throws IOException, RequestException;
}
