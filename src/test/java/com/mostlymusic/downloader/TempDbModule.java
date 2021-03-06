package com.mostlymusic.downloader;

import java.io.File;

import com.google.inject.AbstractModule;
import org.fest.util.Files;

/**
 * @author ytaras
 *         Date: 11/10/11
 *         Time: 10:57 AM
 */
class TempDbModule extends AbstractModule {
    @Override
    protected void configure() {
        bindConstant().annotatedWith(DatabaseFilename.class).to(getDatabaseFile());
    }

    private String getDatabaseFile() {
        File file = Files.newTemporaryFolder();
        //noinspection ResultOfMethodCallIgnored
        file.delete();
        return file.getAbsolutePath();
    }
}
