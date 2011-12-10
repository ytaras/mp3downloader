package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;

/**
 *
 * @author ytaras
 */
public interface AccountManagerListener {
    void currentAccountChanged(Account account);
}
