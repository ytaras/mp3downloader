package com.mostlymusic.downloader.manager;

import com.mostlymusic.downloader.dto.Account;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public class AccountManagerTest {
    @Test
    public void shouldSelectCurrentAccount() throws Exception {
        // given
        AccountManager manager = new MapperAccountManager();
        AccountManagerListener listener = mock(AccountManagerListener.class);
        manager.addListener(listener);
        Account account = new Account();
        assertThat(manager.getCurrentAccount()).isNull();
        // when
        manager.setCurrentAccount(account);

        // then
        verify(listener).currentAccountChanged(account);
        assertThat(manager.getCurrentAccount()).isSameAs(account);
    }
}
