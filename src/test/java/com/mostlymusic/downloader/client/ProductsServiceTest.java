package com.mostlymusic.downloader.client;

import org.apache.http.HttpRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 1:45 PM
 */
public class ProductsServiceTest extends BaseHttpClientTestCase {
    @Override
    protected void registerHandler() {
        localTestServer.register("/products", new ProductsHttpHandler());
    }

    @Test
    public void shouldGetProductDescriptions() throws IOException {
        // given
        IProductsService productsService = new ProductsService(serverUrl);

        // when
        List<ProductDto> products = productsService.getProducts(1, 2, 3);

        // then
        assertThat(products).isEqualTo(getProductMocks(1, 2, 3));
    }

    private List<ProductDto> getProductMocks(int... ids) {
        List<ProductDto> result = new LinkedList<ProductDto>();
        for (int id : ids) {
            result.add(new ProductDto(id));
        }
        return result;
    }


    private class ProductsHttpHandler extends JsonHttpHandler {
        @Override
        protected Object getObject(HttpRequest httpRequest) throws Exception {
            List<NameValuePair> parse = URLEncodedUtils.parse(URI.create(httpRequest.getRequestLine().getUri()), null);
            for (NameValuePair pair : parse) {
                if (pair.getName().equals(IProductsService.ID_PARAM_NAME)) {
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
