package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.client.IOrdersService;
import com.mostlymusic.downloader.client.OrdersService;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 2:14 PM
 */
public class DownloaderModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IOrdersService.class).to(OrdersService.class);
    }
}
