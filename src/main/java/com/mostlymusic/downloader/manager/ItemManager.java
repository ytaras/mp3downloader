package com.mostlymusic.downloader.manager;

import java.util.List;

import com.mostlymusic.downloader.dto.Item;

/**
 *
 * @author ytaras
 */
public interface ItemManager {
    void addListener(ItemMapperListener itemMapperListener);

    void saveItem(Item item);

    List<Item> findItemByStatus(String status);

    List<Item> findItem();

    void setStatus(long itemId, String status);
}
