package com.mostlymusic.downloader.client;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:58 AM
 */
public class OrdersService implements IOrdersService {
    private String serviceUrl;
        private HttpClient httpClient = new DefaultHttpClient();

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public OrdersMetadataDto getOrdersMetadata() throws IOException {
         HttpGet get = new HttpGet(serviceUrl);
        HttpResponse execute = httpClient.execute(get);
        // TODO Deal with encodings
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        execute.getEntity().writeTo(byteArrayOutputStream);
        String data = byteArrayOutputStream.toString();

        return new Gson().fromJson(data, OrdersMetadataDto.class);
    }
}
