package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import org.fest.util.Files;

import java.io.File;

/**
 * @author ytaras
 *         Date: 11/10/11
 *         Time: 10:57 AM
 */
public class TempDbModule extends AbstractModule {
    @Override
    protected void configure() {
        bindConstant().annotatedWith(DatabaseFilename.class).to(getDatabaseFile());
    }

    private String getDatabaseFile() {
        File file = Files.newTemporaryFolder();
        file.delete();
        return file.getAbsolutePath();
    }
}
