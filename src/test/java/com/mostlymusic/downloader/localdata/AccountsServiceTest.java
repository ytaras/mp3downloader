package com.mostlymusic.downloader.localdata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.LocalStorageModule;
import com.mostlymusic.downloader.dto.Account;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:28 PM
 */
public class AccountsServiceTest extends StoragetTestBase {

    private AccountMapper accountMapper;
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new LocalStorageModule());
        accountMapper = injector.getInstance(AccountMapper.class);
        dataSource = injector.getInstance(DataSource.class);
        injector.getInstance(SchemaCreator.class).createTables();
    }

    @Test
    public void shouldInitMyBatis() throws Exception {
        // given
        // when
        List<Account> accounts = accountMapper.listAccounts();
        // then
        assertThat(accounts).isNotNull();
    }

    @Test
    public void shouldCR() throws SQLException {
        // given
        Connection connection = dataSource.getConnection();
        connection.prepareStatement("DELETE FROM ACCOUNTS").executeUpdate();
        connection.close();
        Account account = new Account("ytaras", "password");

        // when
        accountMapper.createAccount(account);

        // then
        List<Account> accounts = accountMapper.listAccounts();
        Account actual = accounts.get(0);
        assertThat(accounts).hasSize(1);
        assertThat(actual.getUsername()).isEqualTo(account.getUsername());
        assertThat(actual.getPassword()).isEqualTo(account.getPassword());
    }

    @Test
    public void shouldCD() throws SQLException {
        // given
        Connection connection = dataSource.getConnection();
        connection.prepareStatement("DELETE FROM ACCOUNTS").executeUpdate();
        connection.close();
        Account account = new Account("ytaras", "password");
        accountMapper.createAccount(account);
        List<Account> accounts = accountMapper.listAccounts();
        Account actual = accounts.get(0);

        // when

        accountMapper.deleteAccount(actual.getId());

        // then
        assertThat(accountMapper.listAccounts()).isEmpty();
    }

}
