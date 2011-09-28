package com.mostlymusic.downloader.localdata;

import com.mostlymusic.downloader.dto.Account;
import org.junit.After;
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
public class AccountsMapperTest extends StoragetTestBase {

    private AccountMapper accountMapper;
    private DataSource dataSource;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        accountMapper = injector.getInstance(AccountMapper.class);
        dataSource = injector.getInstance(DataSource.class);
        injector.getInstance(SchemaCreator.class).createTables();
        injector.getInstance(DataSource.class).getConnection().prepareStatement("DELETE FROM " + AccountMapper.TABLE_NAME);
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
    public void shouldFindByLoginName() {
        // given
        Account account = new Account("ytaras");
        accountMapper.createAccount(account);

        // when
        Account loaded = accountMapper.findByLoginName("ytaras");

        // then
        assertThat(loaded).isNotNull();
        assertThat(loaded.getUsername()).isEqualTo("ytaras");
    }

    @Test
    public void shouldCR() throws SQLException {
        // given
        Account account = new Account("ytaras");
        account.setLastOrderId(14L);

        // when
        accountMapper.createAccount(account);

        // then
        List<Account> accounts = accountMapper.listAccounts();
        Account actual = accounts.get(0);
        assertThat(accounts).hasSize(1);
        assertThat(actual.getUsername()).isEqualTo(account.getUsername());
        assertThat(actual.getLastOrderId()).isEqualTo(14L);
    }

    @Test
    public void shouldUpdate() {
        // given
        Account account = new Account("ytaras");
        accountMapper.createAccount(account);
        account = accountMapper.listAccounts().get(0);
        account.setLastOrderId(12L);
        account.setUsername("123");
        // when
        accountMapper.updateAccount(account);

        // then
        // Not saving password
        Account actual = accountMapper.listAccounts().get(0);
        assertThat(actual).isEqualTo(account);
    }

    @Test
    public void shouldCD() throws SQLException {
        // given
        Account account = new Account("ytaras");
        accountMapper.createAccount(account);
        List<Account> accounts = accountMapper.listAccounts();
        Account actual = accounts.get(0);

        // when
        accountMapper.deleteAccount(actual.getId());

        // then
        assertThat(accountMapper.listAccounts()).isEmpty();
    }

    @After
    public void tearDown() throws Exception {
        Connection connection = dataSource.getConnection();
        try {
            connection.prepareStatement("DELETE FROM ACCOUNTS").executeUpdate();
        } finally {
            connection.close();
        }

    }
}
