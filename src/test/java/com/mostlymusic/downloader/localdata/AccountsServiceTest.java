package com.mostlymusic.downloader.localdata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.LocalStorageModule;
import com.mostlymusic.downloader.dto.Account;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:28 PM
 */
public class AccountsServiceTest extends StoragetTestBase {

    @Test
    public void shouldInitMyBatis() throws Exception {
        // given
        Injector injector = Guice.createInjector(new LocalStorageModule());
        AccountMapper accountMapper = injector.getInstance(AccountMapper.class);
        // when
        List<Account> accounts = accountMapper.listAccounts();
        // then
        assertThat(accounts).isNotNull();
    }
}
