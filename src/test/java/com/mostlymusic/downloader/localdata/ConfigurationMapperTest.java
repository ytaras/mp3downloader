package com.mostlymusic.downloader.localdata;

import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/27/11
 *         Time: 4:12 PM
 */
public class ConfigurationMapperTest extends StoragetTestBase {

    private ConfigurationMapper configurationMapper;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        injector.getInstance(DataSource.class).getConnection()
                .prepareStatement("DROP TABLE " + ConfigurationMapper.TABLE_NAME).execute();
        configurationMapper = injector.getInstance(ConfigurationMapper.class);
        injector.getInstance(SchemaCreator.class).createTables();
    }

    @Test
    public void shouldSaveConfigPath() throws Exception {
        // given
        assertThat(configurationMapper.getDownloadPath()).isEqualTo(System.getProperty("user.home") + "/Downloads");
        // when
        configurationMapper.setDownloadPath("A path");

        // then
        assertThat(configurationMapper.getDownloadPath()).isEqualTo("A path");
    }

    @Test
    public void shouldSaveRefreshRate() throws SQLException {
        // given
        assertThat(configurationMapper.getRefreshRate()).isEqualTo(5);

        // when
        configurationMapper.setRefreshRate(6);

        // then
        assertThat(configurationMapper.getRefreshRate()).isEqualTo(6);
    }
}
