package com.mostlymusic.downloader.client;

import com.google.inject.Injector;
import com.mostlymusic.downloader.MockInjectors;
import com.mostlymusic.downloader.client.exceptions.RequestException;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 1:45 PM
 */
public class ProductsServiceTest extends BaseHttpClientTestCase {

    private ProductsService productsService;

    @Override
    protected void registerHandler() {
        localTestServer.register("/download-manager/sync/productsInfo/", new ProductsHttpHandler());
    }

    @Before
    public void setUp() throws Exception {
        Injector injector = MockInjectors.downloader(serverUrl);
        productsService = injector.getInstance(ProductsService.class);
    }

    @Test
    public void shouldGetProductDescriptions() throws IOException, RequestException {
        // given
        // when
        List<Product> products = productsService.getProducts(Arrays.asList(1L, 2L, 3L));

        // then
        assertThat(products).isEqualTo(getProductMocks(1, 2, 3));
    }

    private List<Product> getProductMocks(int... ids) {
        List<Product> result = new LinkedList<Product>();
        for (int id : ids) {
            result.add(new Product(id));
        }
        return result;
    }


    private class ProductsHttpHandler extends JsonHttpHandler {
        @Override
        protected Object getObject(HttpEntityEnclosingRequest httpRequest) throws Exception {
            List<NameValuePair> parse = URLEncodedUtils.parse(httpRequest.getEntity());
            for (NameValuePair pair : parse) {
                if (pair.getName().equals(ProductsService.ID_PARAM_NAME)) {
                    String[] strIds = pair.getValue().split(",");
                    int[] ids = new int[strIds.length];
                    for (int i = 0; i < strIds.length; i++) {
                        ids[i] = Integer.parseInt(strIds[i]);
                    }
                    return getProductMocks(ids);
                }
            }
            throw new RuntimeException("Id not found");
        }
    }
}
