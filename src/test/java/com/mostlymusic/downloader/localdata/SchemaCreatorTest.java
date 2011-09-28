package com.mostlymusic.downloader.localdata;

import com.google.inject.Guice;
import com.mostlymusic.downloader.LocalStorageModule;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.File;
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
        File tempFile = File.createTempFile("mostly", "db");
        assertThat(tempFile.delete()).isTrue();
        injector = Guice.createInjector(new LocalStorageModule(tempFile));
    }

    @Test
    public void shouldCreateSchema() throws Exception {
        // given
        DataSource dataSource = injector.getInstance(DataSource.class);
        SchemaCreator instance = injector.getInstance(SchemaCreator.class);

        // when
        instance.createTables();

        // then
        assertThat(tableExists(dataSource, AccountMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ItemMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ProductMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ArtistMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ConfigurationMapper.TABLE_NAME)).isTrue();
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
        assertThat(tableExists(dataSource, AccountMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ItemMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ProductMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ArtistMapper.TABLE_NAME)).isTrue();
        assertThat(tableExists(dataSource, ConfigurationMapper.TABLE_NAME)).isTrue();
        ResultSet resultSet = dataSource.getConnection()
                .prepareStatement("SELECT COUNT(*) FROM " + ConfigurationMapper.TABLE_NAME).executeQuery();
        resultSet.next();
        assertThat(resultSet.getInt(1)).as("count of configuration records").isEqualTo(1);
    }

    private boolean tableExists(DataSource dataSource, String tableName) throws SQLException {
        ResultSet tables = dataSource.getConnection().getMetaData().getTables(null, null, tableName, null);
        return tables.next();

    }
}

