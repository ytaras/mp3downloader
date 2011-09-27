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
                .prepareStatement("DELETE FROM " + ConfigurationMapper.TABLE_NAME).execute();
        configurationMapper.insertConfig();
    }

    @Test
    public void shouldSaveFilePath() throws Exception {
        // given
        assertThat(configurationMapper.getDownloadPath()).isNull();
        // when
        configurationMapper.setDownloadPath("A path");

        // then
        assertThat(configurationMapper.getDownloadPath()).isEqualTo("A path");
    }
}
