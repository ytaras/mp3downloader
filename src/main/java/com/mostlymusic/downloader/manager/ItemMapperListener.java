package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public interface ItemMapperListener {
    void addedItem(Item item, Account account);

    void updatedItem(Item item, Account account);
}
