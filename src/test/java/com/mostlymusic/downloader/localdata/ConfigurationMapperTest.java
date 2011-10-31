package com.mostlymusic.downloader.localdata;

import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;

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
        configurationMapper = injector.getInstance(ConfigurationMapper.class);
        injector.getInstance(SchemaCreator.class).createTables();
        injector.getInstance(DataSource.class).getConnection()
                .prepareStatement("DROP TABLE " + ConfigurationMapper.TABLE_NAME).execute();
        configurationMapper.createSchema();
        configurationMapper.insertConfig();
    }

    @Test
    public void shouldSaveConfigPath() throws Exception {
        // given
        assertThat(configurationMapper.getDownloadPath()).isNull();
        // when
        configurationMapper.setDownloadPath("A path");

        // then
        assertThat(configurationMapper.getDownloadPath()).isEqualTo("A path");
    }

    @Test
    public void shouldSaveRefreshRate() {
        // given
        assertThat(configurationMapper.getRefreshRate()).isEqualTo(5 * 60 * 1000);

        // when
        configurationMapper.setRefreshRate(987654321);

        // then
        assertThat(configurationMapper.getRefreshRate()).isEqualTo(987654321);
    }
}
