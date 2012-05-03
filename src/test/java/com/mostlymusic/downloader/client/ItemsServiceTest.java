package com.mostlymusic.downloader.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

import com.mostlymusic.downloader.client.exceptions.RequestException;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.dto.ItemsDto;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.fail;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:50 AM
 */
public class ItemsServiceTest extends BaseHttpClientTestCase {

    private ItemsService itemsService;

    @Override
    protected void registerHandler() {
        localTestServer.register("/download-manager/sync/itemsStatus/", new OrdersHttpHandler());
        localTestServer.register("/download-manager/sync/itemsList/", new TracksHttpHandler());
        localTestServer.register("/download-manager/files/download/id/*", new Mp3FileHttpHandler());
    }

    @Before
    public void createInstance() {
        itemsService = injector.getInstance(ItemsService.class);
    }


    @Test
    public void shouldGetWithoutParameters() throws IOException, RequestException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        ItemsMetadataDto dto = itemsService.getOrdersMetadata(null);

        // then
        assertThat(dto).isEqualTo(getMockOrdersMetadata());
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldGetMetadataWithParameters() throws IOException, RequestException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        ItemsMetadataDto dto = itemsService.getOrdersMetadata(234L);

        // then
        assertThat(dto).isEqualTo(getMockOrdersMetadata(234));
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldReturnList() throws IOException, RequestException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();
        long firstOrderId = 10;
        long lastOrderId = 123;
        int page = 2;
        int pageSize = 100;


        // when
        ItemsDto dtoWithoutFirst = itemsService.getTracks(null, lastOrderId, page, pageSize);

        ItemsDto dtoWithFirst = itemsService.getTracks(firstOrderId, lastOrderId, page, pageSize);

        // then
        assertThat(dtoWithoutFirst).isEqualTo(getMockTracksDtos(lastOrderId, page, pageSize));
        assertThat(dtoWithFirst).isEqualTo(getMockTracksDtos(firstOrderId, lastOrderId, page, pageSize));
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldThrowException() throws Exception {
        // given
        localTestServer.register("/download-manager/sync/itemsStatus/", new FailHttpHandler());
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        try {
            itemsService.getOrdersMetadata(null);
            fail("Exception should be thrown");
        } catch (HttpResponseException e) {
            // then
            assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
            assertThat(e).hasMessage("Invalid request");
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
        }
    }

    @Test
    public void shouldDownloadFile() throws IOException, RequestException {
        // given
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();
        Item item = new Item();
        item.setLinkHash("HASH");
        // when
        Map.Entry<InputStream, Long> trackEntry = itemsService.getTrack(item);

        // then
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        IOUtils.copy(trackEntry.getKey(), byteArrayOutputStream);
        assertThat(byteArrayOutputStream.toString()).isEqualTo(generateContent("HASH"));
        assertThat(trackEntry.getValue()).isPositive();
    }

    private String generateContent(String hash) {
        StringBuilder builder = new StringBuilder(hash);
        for (int i = 0; i < 5000; i++) {
            builder.append(hash);
        }
        return builder.toString();
    }


    private ItemsDto getMockTracksDtos(long firstItemId, long lastItemId, int page, int pageSize) {
        ItemsDto itemsDto = new ItemsDto();
        itemsDto.getInfo().setPageSize(pageSize);
        itemsDto.getInfo().setPageCurrent(page);
        itemsDto.getInfo().setPageTotal((int) lastItemId);
        itemsDto.getInfo().setTotalRecords((int) firstItemId);

        Item item = new Item();
        item.setItemId(1);
        item.setLinkTitle("\u1234Name");
        itemsDto.getItems().add(item);
        return itemsDto;
    }

    private ItemsDto getMockTracksDtos(long lastItemId, int page, int pageSize) {
        return getMockTracksDtos(0, lastItemId, page, pageSize);
    }

    private ItemsMetadataDto getMockOrdersMetadata(long lastOrderId) {
        return new ItemsMetadataDto(lastOrderId, 555);
    }

    ItemsMetadataDto getMockOrdersMetadata() {
        return new ItemsMetadataDto(123, 345);
    }

    private class OrdersHttpHandler extends JsonHttpHandler<HttpEntityEnclosingRequest> {
        @Override
        protected Object getObject(HttpEntityEnclosingRequest httpRequest) throws URISyntaxException, IOException {
            for (NameValuePair pair : URLEncodedUtils.parse(httpRequest.getEntity())) {
                if (pair.getName().equals(ItemsService.FIRST_ITEM_ID_PARAM_NAME)) {
                    Long aLong = Long.parseLong(pair.getValue());
                    return getMockOrdersMetadata(aLong);
                }
            }
            return getMockOrdersMetadata();
        }
    }

    private class TracksHttpHandler extends JsonHttpHandler<HttpEntityEnclosingRequest> {

        @Override
        protected Object getObject(HttpEntityEnclosingRequest httpRequest) throws URISyntaxException, IOException {
            long lastItemId = 0;
            int page = 0;
            int pageSize = 0;
            long firstItemId = 0;
            for (NameValuePair pair : URLEncodedUtils.parse(httpRequest.getEntity())) {
                String name = pair.getName();
                if (name.equals(ItemsService.LAST_ITEM_ID_PARAM_NAME)) {
                    lastItemId = Long.parseLong(pair.getValue());
                } else if (ItemsService.PAGE_PARAM_NAME.equals(name)) {
                    page = Integer.parseInt(pair.getValue());
                } else if (ItemsService.PAGE_SIZE_PARAM_NAME.equals(name)) {
                    pageSize = Integer.parseInt(pair.getValue());
                } else if (ItemsService.FIRST_ITEM_ID_PARAM_NAME.equals(name)) {
                    firstItemId = Long.parseLong(pair.getValue());
                }
            }
            if (0 == lastItemId) {
                throw new RuntimeException("lastItemId should be set ");
            } else if (0 == page) {
                throw new RuntimeException("page should be set ");
            } else if (0 == pageSize) {
                throw new RuntimeException("pageSize should be set ");
            }
            return getMockTracksDtos(firstItemId, lastItemId, page, pageSize);
        }
    }

    private class FailHttpHandler extends JsonHttpHandler {

        @Override
        protected Object getObject(HttpRequest httpRequest) {
            throw new RuntimeException("Invalid request");
        }
    }

    private class Mp3FileHttpHandler implements HttpRequestHandler {
        @Override
        public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
            String[] uriPart = httpRequest.getRequestLine().getUri().split("/");
            String hash = uriPart[uriPart.length - 1];
            httpResponse.setEntity(new StringEntity(generateContent(hash)));
        }
    }

}
