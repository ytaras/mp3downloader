package com.mostlymusic.downloader;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.client.*;
import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
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

        bind(Gson.class).toInstance(createGson());
        DefaultHttpClient defaultHttpClient = createHttpClientInstance();

        bind(DefaultHttpClient.class).toInstance(defaultHttpClient);

        bind(IItemsService.class).to(ItemsService.class);
        bind(IProductsService.class).to(ProductsService.class);
        bind(IAuthService.class).to(AuthService.class);
    }

    private Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    private DefaultHttpClient createHttpClientInstance() {
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
        defaultHttpClient.setCookieStore(new BasicCookieStore());
        if (System.getProperties().getProperty("http.proxyHost") != null) {
            HttpHost proxy = new HttpHost(System.getProperty("http.proxyHost"),
                    Integer.parseInt(System.getProperty("http.proxyPort")), "http");
            defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        return defaultHttpClient;
    }
}
