package com.mostlymusic.downloader.localdata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.LocalStorageModule;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 6:05 PM
 */
public class ConnectionManagerTest {

    private IConnectionManager connectionManager;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new LocalStorageModule());
        connectionManager = injector.getInstance(IConnectionManager.class);
    }

    @Test
    public void shouldCreateDatabaseAndTable() throws ClassNotFoundException, SQLException, IOException {
        // given
        ensureDatabaseFileNotExist();

        // when
        connectionManager.initDatabase();

        // then
        assertThat(getDbFile()).exists();
    }

    private void ensureDatabaseFileNotExist() throws IOException {
        File file = getDbFile();
        if (file.exists()) {
            FileUtils.deleteDirectory(file);
        }
    }

    private File getDbFile() {
        String userHome = System.getProperty("user.home");
        return new File(userHome, ".mostlymusic.db");
    }

}
