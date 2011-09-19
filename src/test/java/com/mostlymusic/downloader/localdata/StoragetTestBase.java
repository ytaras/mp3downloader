package com.mostlymusic.downloader.localdata;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:52 PM
 */
public class StoragetTestBase {
    protected void ensureDatabaseFileNotExist() throws IOException {
        File file = getDbFile();
        if (file.exists()) {
            FileUtils.deleteDirectory(file);
        }
    }

    protected File getDbFile() {
        String userHome = System.getProperty("user.home");
        return new File(userHome, ".mostlymusic.db");
    }
}
