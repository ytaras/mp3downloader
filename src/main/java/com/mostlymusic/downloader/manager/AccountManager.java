package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public interface AccountManager {
    void addListener(AccountManagerListener mock);

    void setCurrentAccount(Account account);

    Account getCurrentAccount();
}
