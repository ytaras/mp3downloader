package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;

/**
 *
 * @author ytaras
 */
public interface AccountManager {
    void addListener(AccountManagerListener mock);

    void setCurrentAccount(Account account);

    Account getCurrentAccount();
}
