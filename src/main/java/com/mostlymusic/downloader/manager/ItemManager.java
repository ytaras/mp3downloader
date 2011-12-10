package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public interface ItemManager {
    void addListener(ItemMapperListener itemMapperListener);

    void saveItem(Item item, Account account);
}
