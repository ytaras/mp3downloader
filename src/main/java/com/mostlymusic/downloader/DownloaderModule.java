package com.mostlymusic.downloader;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.client.*;
import org.apache.http.impl.client.BasicCookieStore;
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
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        defaultHttpClient.setCookieStore(new BasicCookieStore());
        bind(DefaultHttpClient.class).toInstance(defaultHttpClient);

        bind(IOrdersService.class).to(OrdersService.class);
        bind(IProductsService.class).to(ProductsService.class);
        bind(IAuthService.class).to(AuthService.class);
    }
}
