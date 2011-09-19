package com.mostlymusic.downloader.client;

import org.apache.http.HttpRequest;
import org.junit.Test;

import java.io.IOException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:50 AM
 */
public class OrdersServiceTest extends BaseHttpClientTestCase {
    @Override
    protected void registerHandler() {
        localTestServer.register("/orders/", new OrdersHttpHandler());
    }

    @Test
    public void shouldGetWithoutParameters() throws IOException {
        // given
        OrdersService ordersService = new OrdersService();
        ordersService.setServiceUrl(serverUrl + "/orders/");
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        OrdersMetadataDto dto = ordersService.getOrdersMetadata();

        // then
        assertThat(dto).isEqualTo(getMockOrdersMetadata());
    }

    protected OrdersMetadataDto getMockOrdersMetadata() {
        return new OrdersMetadataDto(123, 345);
    }

    private class OrdersHttpHandler extends JsonHttpHandler {
        @Override
        protected Object getObject(HttpRequest httpRequest) {
            return getMockOrdersMetadata();
        }
    }
}
