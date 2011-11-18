package com.mostlymusic.downloader.localdata;

import com.mostlymusic.downloader.MockInjectors;
import com.mostlymusic.downloader.dto.Account;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 5:17 PM
 */
public class SchemaCreatorTest extends StoragetTestBase {

    @Override
    public void setUp() throws Exception {
        DataSource dataSource = injector.getInstance(DataSource.class);
        Connection connection = dataSource.getConnection();
        try {
            ResultSet tables = connection.getMetaData().getTables(null, "APP", null, null);
            while (tables.next()) {
                dropTable(connection, tables.getString("TABLE_NAME"));
            }
        } finally {
            connection.close();
        }
    }

    @BeforeClass
    public static void createDatabase() throws IOException {
        injector = MockInjectors.storageTempDb(true);
    }

    @Test
    public void shouldCreateSchema() throws Exception {
        // given
        DataSource dataSource = injector.getInstance(DataSource.class);
        SchemaCreator instance = injector.getInstance(SchemaCreator.class);

        // when
        instance.createTables();

        // then
        Connection connection = dataSource.getConnection();
        try {
            assertThat(tableExists(connection, AccountMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, ItemMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, ProductMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, ArtistMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, ConfigurationMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, VersionMapper.TABLE_NAME)).isTrue();
        } finally {
            connection.close();
        }
    }

    @Test
    public void shouldNotCreateExisting() throws SQLException {
        // given
        SchemaCreator instance = injector.getInstance(SchemaCreator.class);

        // when
        instance.createTables();
        instance.createTables();

        // then
        DataSource dataSource = injector.getInstance(DataSource.class);
        Connection connection = dataSource.getConnection();
        try {
            assertThat(tableExists(connection, AccountMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, ItemMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, ProductMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, ArtistMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, ConfigurationMapper.TABLE_NAME)).isTrue();
            assertThat(tableExists(connection, VersionMapper.TABLE_NAME)).isTrue();
            ResultSet resultSet = connection.prepareStatement("SELECT COUNT(*) FROM " + ConfigurationMapper.TABLE_NAME)
                    .executeQuery();
            resultSet.next();
            assertThat(resultSet.getInt(1)).as("count of configuration records").isEqualTo(1);
        } finally {
            connection.close();
        }
    }

    @Test
    public void shouldRecreateSchemaIfNoVersionTable() throws SQLException {
        // given
        DataSource dataSource = injector.getInstance(DataSource.class);
        injector.getInstance(SchemaCreator.class).createTables();
        Connection connection = dataSource.getConnection();
        try {
            dropTable(connection, VersionMapper.TABLE_NAME);
        } finally {
            connection.close();
        }
        AccountMapper accountMapper = injector.getInstance(AccountMapper.class);
        accountMapper.createAccount(new Account("user"));
        assertThat(accountMapper.listLoginNames("")).isNotEmpty();

        // when
        injector.getInstance(SchemaCreator.class).createTables();

        // then
        assertThat(accountMapper.listLoginNames("")).isEmpty();
    }

    @Test
    public void shouldMigrateToVersion2() throws SQLException {
        // given
        SchemaCreator instance = injector.getInstance(SchemaCreator.class);
        instance.migrateTo(1);
        DataSource dataSource = injector.getInstance(DataSource.class);
        Connection connection = dataSource.getConnection();
        String autoDownloadColumnNane = "autoDownload".toUpperCase();
        String threadCount = "threadCount".toUpperCase();
        try {
            assertThat(columnExists(connection, AccountMapper.TABLE_NAME, "lastOrderId".toUpperCase())).isTrue();
            assertThat(columnExists(connection, ConfigurationMapper.TABLE_NAME, autoDownloadColumnNane)).isFalse();
            assertThat(columnExists(connection, ConfigurationMapper.TABLE_NAME, threadCount)).isFalse();

            // when
            instance.migrateTo(2);

            // then
            assertThat(columnExists(connection, ConfigurationMapper.TABLE_NAME, autoDownloadColumnNane)).isTrue();
            assertThat(columnExists(connection, ConfigurationMapper.TABLE_NAME, threadCount)).isTrue();
        } finally {
            connection.close();
        }
    }

    private void dropTable(Connection connection, String tableName) throws SQLException {
        connection.prepareStatement("DROP TABLE " + tableName).execute();
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, tableName, null);
        return tables.next();
    }

    private boolean columnExists(Connection connection, String tableName, String columnName) throws SQLException {
        ResultSet tables = connection.getMetaData().getColumns(null, null, tableName, columnName);
        return tables.next();
    }
}

