package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public interface AccountManagerListener {
    void currentAccountChanged(Account account);
}
