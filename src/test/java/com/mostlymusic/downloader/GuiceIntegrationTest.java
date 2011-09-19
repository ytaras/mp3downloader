package com.mostlymusic.downloader;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.client.IOrdersService;
import org.junit.Test;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 2:11 PM
 */
public class GuiceIntegrationTest {

    @Test
    public void shouldCreateInjector() throws Exception {
        Injector injector = Guice.createInjector(new DownloaderModule());
        IOrdersService instance = injector.getInstance(IOrdersService.class);
    }
}
