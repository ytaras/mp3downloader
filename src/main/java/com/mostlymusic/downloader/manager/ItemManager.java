package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Item;

import java.util.List;

/**
 *
 * @author ytaras
 */
public interface ItemManager {
    void addListener(ItemMapperListener itemMapperListener);

    void saveItem(Item item);

    List<Item> findItemByStatus(String status);

    List<Item> findItem();
}
