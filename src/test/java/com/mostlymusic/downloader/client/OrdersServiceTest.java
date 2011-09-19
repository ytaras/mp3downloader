package com.mostlymusic.downloader.client;

import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
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

    @Override
    protected void registerHandler() {
        localTestServer.register("/orders/", new OrdersHttpHandler());
        localTestServer.register("/orders/list", new TracksHttpHandler());
        localTestServer.register("/fail/", new FailHttpHandler());
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
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldGetMetadataWithParameters() throws IOException {
        // given
        OrdersService ordersService = new OrdersService();
        ordersService.setServiceUrl(serverUrl + "/orders/");
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        OrdersMetadataDto dto = ordersService.getOrdersMetadata(234);

        // then
        assertThat(dto).isEqualTo(getMockOrdersMetadata(234));
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldReturnList() throws IOException {
        // given
        OrdersService ordersService = new OrdersService();
        ordersService.setServiceUrl(serverUrl + "/orders/");
        assertThat(localTestServer.getAcceptedConnectionCount()).isZero();

        // when
        List<TrackDto> dto = ordersService.getTracks();

        // then
        assertThat(dto).isEqualTo(getMockTracksDtos());
        assertThat(localTestServer.getAcceptedConnectionCount()).isPositive();
    }

    @Test
    public void shouldThrowException() throws IOException {
        // given
        OrdersService ordersService = new OrdersService();
        ordersService.setServiceUrl(serverUrl + "/fail/");
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


    private List<TrackDto> getMockTracksDtos() {
        LinkedList<TrackDto> trackDtos = new LinkedList<TrackDto>();
        TrackDto trackDto = new TrackDto();
        trackDto.setId(1);
        trackDto.setAlbum("A\u1234lbum");
        trackDto.setName("Name");
        trackDto.setArtist("Artist");
        trackDtos.add(trackDto);
        return trackDtos;
    }

    private OrdersMetadataDto getMockOrdersMetadata(long lastOrderId) {
        return new OrdersMetadataDto(lastOrderId, 555);
    }

    protected OrdersMetadataDto getMockOrdersMetadata() {
        return new OrdersMetadataDto(123, 345);
    }

    private class OrdersHttpHandler extends JsonHttpHandler {
        @Override
        protected Object getObject(HttpRequest httpRequest) {
            String uri = httpRequest.getRequestLine().getUri();
            List<NameValuePair> parse;
            try {
                parse = URLEncodedUtils.parse(new URI(uri), null);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            for (NameValuePair nameValuePair : parse) {
                if (nameValuePair.getName().equals(IOrdersService.LAST_ORDER_ID_PARAM_NAME)) {
                    Long aLong = Long.parseLong(nameValuePair.getValue());
                    return getMockOrdersMetadata(aLong);
                }
            }
            return getMockOrdersMetadata();
        }
    }

    private class TracksHttpHandler extends JsonHttpHandler {
        @Override
        protected Object getObject(HttpRequest httpRequest) {
            return getMockTracksDtos();
        }
    }

    private class FailHttpHandler extends JsonHttpHandler {
        @Override
        protected boolean requestValid(HttpRequest httpRequest) {
            reason = "Invalid request";
            return false;
        }

        @Override
        protected Object getObject(HttpRequest httpRequest) {
            return null;
        }
    }

}
