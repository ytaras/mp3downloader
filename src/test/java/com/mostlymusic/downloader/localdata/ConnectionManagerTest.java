package com.mostlymusic.downloader.localdata;

import org.apache.commons.io.FileUtils;
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

    @Test
    public void shouldCreateDatabaseAndTable() throws ClassNotFoundException, SQLException, IOException {
        // given
        ensureDatabaseFileNotExist();
        // when
        IConnectionManager manager = new ConnectionManager();
        manager.getConnection();

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
