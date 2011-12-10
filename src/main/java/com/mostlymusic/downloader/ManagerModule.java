package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.manager.AccountManager;
import com.mostlymusic.downloader.manager.ItemManager;
import com.mostlymusic.downloader.manager.MapperAccountManager;
import com.mostlymusic.downloader.manager.MapperItemManager;

/**
 * @author ytaras
 */
public class ManagerModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ItemManager.class).to(MapperItemManager.class);
        bind(AccountManager.class).to(MapperAccountManager.class);
    }
}
