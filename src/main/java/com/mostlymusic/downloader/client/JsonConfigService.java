package com.mostlymusic.downloader.client;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.ServiceUrl;
import com.mostlymusic.downloader.client.exceptions.RequestException;
import org.apache.http.client.methods.HttpGet;

/**
 * Created with IntelliJ IDEA.
 * User: ytaras
 * Date: 28.04.12
 * Time: 9:46
 */
@Singleton
public class JsonConfigService extends JsonServiceClient implements ConfigService {
    private String serviceUrl;
    private Config config;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    @Inject
    public JsonConfigService(@ServiceUrl String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public Config getConfig() throws IOException, RequestException {
        // No synchronization here - worst case of race conditioning is 2 requests instead of one
        if (config == null) {
            return config = createConfig();
        }
        return config;
    }

    private Config createConfig() throws IOException, RequestException {
        HttpGet get = new HttpGet(serviceUrl + "/download-manager/sync/config/");
        return getResult(get, Config.class);
    }
}
