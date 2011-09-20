package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:50 AM
 */
public class OrdersServiceTest extends BaseHttpClientTestCase {

    private IOrdersService ordersService;

    @Override
    protected void registerHandler() {
        localTestServer.register("/itemsStatus/", new OrdersHttpHandler());
        localTestServer.register("/itemsList/", new TracksHttpHandler());
    }

    @Before
    public void createInstance() {
        ordersService = injector.getInstance(IOrdersService.class);
    }


    @Test
    public void shouldGetWithoutParameters() throws IOException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        ItemsMetadataDto dto = ordersService.getOrdersMetadata();

        // then
        assertThat(dto).isEqualTo(getMockOrdersMetadata());
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldGetMetadataWithParameters() throws IOException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        ItemsMetadataDto dto = ordersService.getOrdersMetadata(234);

        // then
        assertThat(dto).isEqualTo(getMockOrdersMetadata(234));
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldReturnList() throws IOException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        List<ItemDto> dto = ordersService.getTracks(123, 2, 100);

        // then
        assertThat(dto).isEqualTo(getMockTracksDtos());
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldThrowException() throws Exception {
        // given
        localTestServer.register("/itemsStatus/", new FailHttpHandler());
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        try {
            ordersService.getOrdersMetadata();
            fail("Exception should be thrown");
        } catch (HttpResponseException e) {
            // then
            assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
            assertThat(e).hasMessage("Invalid request");
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        }
    }


    private List<ItemDto> getMockTracksDtos() {
        LinkedList<ItemDto> itemDtos = new LinkedList<ItemDto>();
        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(1);
        itemDto.setLinkTitle("\u1234Name");
        itemDtos.add(itemDto);
        return itemDtos;
    }

    private ItemsMetadataDto getMockOrdersMetadata(long lastOrderId) {
        return new ItemsMetadataDto(lastOrderId, 555);
    }

    protected ItemsMetadataDto getMockOrdersMetadata() {
        return new ItemsMetadataDto(123, 345);
    }

    private class OrdersHttpHandler extends JsonHttpHandler {
        @Override
        protected Object getObject(HttpEntityEnclosingRequest httpRequest) throws URISyntaxException, IOException {
            for (NameValuePair pair : URLEncodedUtils.parse(httpRequest.getEntity())) {
                if (pair.getName().equals(IOrdersService.LAST_ORDER_ID_PARAM_NAME)) {
                    Long aLong = Long.parseLong(pair.getValue());
                    return getMockOrdersMetadata(aLong);
                }
            }
            return getMockOrdersMetadata();
        }
    }

    private class TracksHttpHandler extends JsonHttpHandler {

        @Override
        protected Object getObject(HttpEntityEnclosingRequest httpRequest) throws URISyntaxException, IOException {
            long lastOrderId = 0;
            int page = 0;
            int pageSize = 0;
            for (NameValuePair pair : URLEncodedUtils.parse(httpRequest.getEntity())) {
                String name = pair.getName();
                if (name.equals(IOrdersService.LAST_ORDER_ID_PARAM_NAME)) {
                    lastOrderId = Long.parseLong(pair.getValue());
                } else if (IOrdersService.PAGE_PARAM_NAME.equals(name)) {
                    page = Integer.parseInt(pair.getValue());
                } else if (IOrdersService.PAGE_SIZE_PARAM_NAME.equals(name)) {
                    pageSize = Integer.parseInt(pair.getValue());
                }
            }
            if (0 == lastOrderId) {
                throw new RuntimeException("lastOrderId should be set ");
            } else if (0 == page) {
                throw new RuntimeException("page should be set ");
            } else if (0 == pageSize) {
                throw new RuntimeException("pageSize should be set ");
            }
            return getMockTracksDtos();
        }
    }

    private class FailHttpHandler extends JsonHttpHandler {

        @Override
        protected Object getObject(HttpEntityEnclosingRequest httpRequest) {
            throw new RuntimeException("Invalid request");
        }
    }

}
