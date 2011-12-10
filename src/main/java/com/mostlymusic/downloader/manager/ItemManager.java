package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Item;

/**
 *
 * @author ytaras
 */
public interface ItemManager {
    void addListener(ItemMapperListener itemMapperListener);

    void saveItem(Item item);
}
