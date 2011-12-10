package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;

/**
 *
 * @author ytaras
 */
public interface ItemMapperListener {
    void addedItem(Item item, Account account);

    void updatedItem(Item item);
}
