package com.mostlymusic.downloader.localdata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.LocalStorageModule;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 5:17 PM
 */
public class SchemaCreatorTest extends StoragetTestBase {

    @Test
    public void shouldCreateSchema() throws Exception {
        // given
        ensureDatabaseFileNotExist();
        Injector injector = Guice.createInjector(new LocalStorageModule());
        DataSource dataSource = injector.getInstance(DataSource.class);
        SchemaCreator instance = injector.getInstance(SchemaCreator.class);

        // when
        instance.createTables();
        injector.getInstance(AccountMapper.class).listAccounts();

        // then
        assertThat(tableExists(dataSource, AccountMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ItemsMapper.TABLE_NAME)).isTrue();
    }

    @Test
    public void shouldNotCreateExisting() throws SQLException {
        // given
        Injector injector = Guice.createInjector(new LocalStorageModule());
        SchemaCreator instance = injector.getInstance(SchemaCreator.class);

        // when
        instance.createTables();
        instance.createTables();

        // then
        DataSource dataSource = injector.getInstance(DataSource.class);
        assertThat(tableExists(dataSource, AccountMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ItemsMapper.TABLE_NAME)).isTrue();
    }

    private boolean tableExists(DataSource dataSource, String tableName) throws SQLException {
        ResultSet tables = dataSource.getConnection().getMetaData().getTables(null, null, tableName, null);
        return tables.next();

    }
}
