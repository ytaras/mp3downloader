package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.mostlymusic.downloader.localdata.ConnectionManager;
import com.mostlymusic.downloader.localdata.IConnectionManager;
import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.io.File;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:02 PM
 */
public class LocalStorageModule extends AbstractModule {
    @Override
    protected void configure() {
        bindConstant().annotatedWith(DatabaseFilename.class).to(getDatabaseFile());
        bind(IConnectionManager.class).to(ConnectionManager.class);
        bind(DataSource.class).toProvider(DataSourceProvider.class).in(Scopes.SINGLETON);
    }

    private String getDatabaseFile() {
        String userHome = System.getProperty("user.home");
        return new File(userHome, ".mostlymusic.db").getAbsolutePath();
    }

    public static class DataSourceProvider implements Provider<DataSource> {
        private String fileName;

        @Inject
        public DataSourceProvider(@DatabaseFilename String fileName) {
            this.fileName = fileName;
        }

        @Override
        public DataSource get() {
            EmbeddedDataSource ds = new EmbeddedDataSource();
            ds.setCreateDatabase("create");
            ds.setDatabaseName(fileName);
            return ds;
        }
    }
}
