package com.mostlymusic.downloader;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.client.IOrdersService;
import com.mostlymusic.downloader.client.IProductsService;
import com.mostlymusic.downloader.client.OrdersService;
import com.mostlymusic.downloader.client.ProductsService;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 2:14 PM
 */
public class DownloaderModule extends AbstractModule {

    private final String serviceUrl;

    public DownloaderModule(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    protected void configure() {
        bindConstant().annotatedWith(ServiceUrl.class).to(serviceUrl);

        bind(Gson.class).toInstance(new Gson());
        bind(HttpClient.class).toInstance(new DefaultHttpClient(new ThreadSafeClientConnManager()));

        bind(IOrdersService.class).to(OrdersService.class);
        bind(IProductsService.class).to(ProductsService.class);
    }
}
