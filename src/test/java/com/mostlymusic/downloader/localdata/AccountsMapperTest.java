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
    public void shouldListLoginNames() {
        // given
        accountMapper.createAccount(new Account("acc2"));
        accountMapper.createAccount(new Account("bacc1"));
        accountMapper.createAccount(new Account("cacc3"));

        // when
        List<String> loginNames = accountMapper.listLoginNames("");
        List<String> aLoginNames = accountMapper.listLoginNames("a");

        // then
        assertThat(loginNames).containsExactly("acc2", "bacc1", "cacc3");
        assertThat(aLoginNames).containsExactly("acc2");
    }

    @Test
    public void shouldCR() throws SQLException {
        // given
        Account account = new Account("ytaras");
        account.setLastOrderId(14L);

        // when
        accountMapper.createAccount(account);

        // then
        List<String> accounts = accountMapper.listLoginNames("");
        String actual = accounts.get(0);
        assertThat(accounts).hasSize(1);
        assertThat(actual).isEqualTo(account.getUsername());
        assertThat(getFirstAccount().getLastOrderId()).isEqualTo(14L);
    }

    @Test
    public void shouldUpdate() {
        // given
        Account account = new Account("ytaras");
        accountMapper.createAccount(account);
        account = getFirstAccount();
        account.setLastOrderId(12L);
        account.setUsername("123");
        // when
        accountMapper.updateAccount(account);

        // then
        // Not saving password
        Account actual = getFirstAccount();
        assertThat(actual).isEqualTo(account);
    }

    private Account getFirstAccount() {
        String accountName;
        accountName = accountMapper.listLoginNames("").get(0);
        return accountMapper.findByLoginName(accountName);
    }

    @Test
    public void shouldCD() throws SQLException {
        // given
        Account account = new Account("ytaras");
        accountMapper.createAccount(account);
        Account actual = getFirstAccount();

        // when
        accountMapper.deleteAccount(actual.getId());

        // then
        assertThat(accountMapper.listLoginNames("")).isEmpty();
    }

    @Test
    public void shouldSetLastLogin() {
        // given
        accountMapper.createAccount(new Account("acc1"));
        accountMapper.createAccount(new Account("acc2"));
        accountMapper.createAccount(new Account("acc3"));
        accountMapper.createAccount(new Account("acc4"));

        // when
        assertThat(accountMapper.findByLoginName("acc1").isLastLoggedIn()).isFalse();
        accountMapper.setLastLoggedIn("acc1");
        assertThat(accountMapper.findByLoginName("acc1").isLastLoggedIn()).isTrue();
        accountMapper.setLastLoggedIn("acc2");
        assertThat(accountMapper.findByLoginName("acc1").isLastLoggedIn()).isFalse();
        assertThat(accountMapper.findByLoginName("acc2").isLastLoggedIn()).isTrue();

        // then
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
