package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.dto.ItemDto;
import com.mostlymusic.downloader.dto.ItemsDto;
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

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:50 AM
 */
public class ItemsServiceTest extends BaseHttpClientTestCase {

    private IItemsService itemsService;

    @Override
    protected void registerHandler() {
        localTestServer.register("/itemsStatus/", new OrdersHttpHandler());
        localTestServer.register("/itemsList/", new TracksHttpHandler());
    }

    @Before
    public void createInstance() {
        itemsService = injector.getInstance(IItemsService.class);
    }


    @Test
    public void shouldGetWithoutParameters() throws IOException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        ItemsMetadataDto dto = itemsService.getOrdersMetadata();

        // then
        assertThat(dto).isEqualTo(getMockOrdersMetadata());
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldGetMetadataWithParameters() throws IOException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        ItemsMetadataDto dto = itemsService.getOrdersMetadata(234);

        // then
        assertThat(dto).isEqualTo(getMockOrdersMetadata(234));
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldReturnList() throws IOException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        ItemsDto dto = itemsService.getTracks(123, 2, 100);

        // then
        assertThat(dto).isEqualTo(getMockTracksDtos(123, 2, 100));
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldThrowException() throws Exception {
        // given
        localTestServer.register("/itemsStatus/", new FailHttpHandler());
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        try {
            itemsService.getOrdersMetadata();
            fail("Exception should be thrown");
        } catch (HttpResponseException e) {
            // then
            assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
            assertThat(e).hasMessage("Invalid request");
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        }
    }


    private ItemsDto getMockTracksDtos(long lastItemId, int page, int pageSize) {
        ItemsDto itemsDto = new ItemsDto();
        itemsDto.getInfo().setPageSize(pageSize);
        itemsDto.getInfo().setPageCurrent(page);
        itemsDto.getInfo().setPageTotal(12);
        itemsDto.getInfo().setTotalRecords(120);

        ItemDto itemDto = new ItemDto();
        itemDto.setItemId(1);
        itemDto.setLinkTitle("\u1234Name");
        itemsDto.getItems().add(itemDto);
        return itemsDto;
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
                if (pair.getName().equals(IItemsService.FIRST_ITEM_ID_PARAM_NAME)) {
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
            long lastItemId = 0;
            int page = 0;
            int pageSize = 0;
            for (NameValuePair pair : URLEncodedUtils.parse(httpRequest.getEntity())) {
                String name = pair.getName();
                if (name.equals(IItemsService.LAST_ITEM_ID_PARAM_NAME)) {
                    lastItemId = Long.parseLong(pair.getValue());
                } else if (IItemsService.PAGE_PARAM_NAME.equals(name)) {
                    page = Integer.parseInt(pair.getValue());
                } else if (IItemsService.PAGE_SIZE_PARAM_NAME.equals(name)) {
                    pageSize = Integer.parseInt(pair.getValue());
                }
            }
            if (0 == lastItemId) {
                throw new RuntimeException("lastItemId should be set ");
            } else if (0 == page) {
                throw new RuntimeException("page should be set ");
            } else if (0 == pageSize) {
                throw new RuntimeException("pageSize should be set ");
            }
            return getMockTracksDtos(lastItemId, page, pageSize);
        }
    }

    private class FailHttpHandler extends JsonHttpHandler {

        @Override
        protected Object getObject(HttpEntityEnclosingRequest httpRequest) {
            throw new RuntimeException("Invalid request");
        }
    }

}
