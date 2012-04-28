package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import org.apache.http.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 5:50 PM
 */
public class AuthServiceTest extends BaseHttpClientTestCase {
    @Override
    protected void registerHandler() {
        localTestServer.register("/download-manager/sync/loginPost/", new AuthHttpRequestHandler());
        localTestServer.register("/download-manager/sync/itemsStatus/", new JsonHttpHandler<HttpEntityEnclosingRequest>() {
            @Override
            protected Object getObject(HttpEntityEnclosingRequest httpRequest) throws Exception {
                Header[] cookies = httpRequest.getHeaders("Cookie");
                if (cookies.length == 0) {
                    throw new RuntimeException("Cookies not set");
                }
                return new ItemsMetadataDto(0, 0);
            }
        });
    }

    @Test
    public void shouldAuth() throws Exception {
        // given
        AuthService authService = injector.getInstance(AuthService.class);


        // when
        boolean authCookie = authService.auth("name", "pass");

        // then
        assertThat(authCookie).isTrue();
    }

    @Test
    public void shouldNotAuth() throws IOException, RequestException {
        // given
        AuthService authService = injector.getInstance(AuthService.class);


        // when
        boolean isAuth = authService.auth("name", "pass1");

        // then
        assertThat(isAuth).isFalse();
    }

    @Test
    @Ignore
    // TODO Fix test
    public void shouldSendCookie() throws IOException, RequestException {
        // given
        AuthService authService = injector.getInstance(AuthService.class);
        ItemsService itemsService = injector.getInstance(ItemsService.class);

        // when
        authService.auth("name", "pass");

        // then
        itemsService.getOrdersMetadata(null);
    }

    private static class AuthHttpRequestHandler implements HttpRequestHandler {
        @Override
        public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
            List<NameValuePair> parse = URLEncodedUtils.parse(
                    ((HttpEntityEnclosingRequest) httpRequest).getEntity());
            String name = null;
            String password = null;
            for (NameValuePair pair : parse) {
                if (pair.getName().equals(AuthService.USERNAME)) {
                    name = pair.getValue();
                } else if (pair.getName().equals(AuthService.PASSWORD)) {
                    password = pair.getValue();
                }
            }
            if (null == name || null == password) {
                httpResponse.setStatusCode(HttpStatus.SC_BAD_REQUEST);
                httpResponse.setEntity(new StringEntity("name and password should be set"));
            } else if (name.equals("name") && password.equals("pass")) {
                BasicHeader setCookieHeader = createSetCookieHeader(name + password);
                httpResponse.addHeader(setCookieHeader);
            } else {
                httpResponse.setStatusCode(HttpStatus.SC_FORBIDDEN);
            }
        }

        private BasicHeader createSetCookieHeader(String value) {
            BasicClientCookie basicClientCookie = new BasicClientCookie("frontend", value);
            basicClientCookie.setPath("/");
            List<Header> headers = new BrowserCompatSpec().formatCookies(Collections.<Cookie>singletonList(basicClientCookie));
            Header header = headers.get(0);
            return new BasicHeader("Set-Cookie", header.getValue());
        }
    }
}
