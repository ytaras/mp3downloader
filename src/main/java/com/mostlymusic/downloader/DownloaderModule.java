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

        bind(ItemsService.class).to(JsonItemsService.class);
        bind(ProductsService.class).to(JsonProductsService.class);
        bind(AuthService.class).to(PostAuthService.class);
        bind(ArtistsService.class).to(JsonArtistsService.class);
    }

    private Gson createGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    private DefaultHttpClient createHttpClientInstance() {
        ThreadSafeClientConnManager conman = new ThreadSafeClientConnManager();
        conman.setMaxTotal(200);
        conman.setDefaultMaxPerRoute(20);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(conman);
        defaultHttpClient.setCookieStore(new BasicCookieStore());
        configureProxy(defaultHttpClient);
        return defaultHttpClient;
    }

    private void configureProxy(DefaultHttpClient defaultHttpClient) {
        HttpHost httpProxy = null;
        if (System.getProperties().getProperty("http.proxyHost") != null) {
            httpProxy = new HttpHost(System.getProperty("http.proxyHost"),
                    Integer.parseInt(System.getProperty("http.proxyPort")), "http");

        } else if (System.getProperty("javaplugin.proxy.config.list") != null) {
            String proxyList = System.getProperties().getProperty("javaplugin.proxy.config.list").toUpperCase();
            System.out.println("PROXY: " + proxyList);
            //  Using HTTP proxy as proxy for HTTP proxy tunnelled SSL
            //  socket (should be listed FIRST)....
            //  1/14/03 1.3.1_06 appears to omit HTTP portion of
            //  reported proxy list... Mod to accomodate this...
            //  Expecting proxyList of "HTTP=XXX.XXX.XXX.XXX:Port" OR
            //  "XXX.XXX.XXX.XXX:Port" & assuming HTTP...
            String proxyIP;
            if (proxyList.contains("HTTP=")) {
                proxyIP = proxyList.substring(proxyList.indexOf("HTTP=") + 5, proxyList.indexOf(":"));
            } else {
                proxyIP = proxyList.substring(0, proxyList.indexOf(":"));
            }
            int endOfPort = proxyList.indexOf(",");
            if (endOfPort < 1)
                endOfPort = proxyList.length();
            String portString = proxyList.substring(proxyList.indexOf(":") + 1, endOfPort);
            int proxyPort = Integer.parseInt(portString);
            httpProxy = new HttpHost(proxyIP, proxyPort);
        }
        if (httpProxy != null) {
            defaultHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpProxy);
        }
    }
}
