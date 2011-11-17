package com.mostlymusic.downloader;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.mostlymusic.downloader.localdata.*;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;

import javax.sql.DataSource;
import java.io.File;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:02 PM
 */
public class LocalStorageModule extends MyBatisModule {
    private File dbFile;

    public LocalStorageModule() {
    }

    public LocalStorageModule(File dbFile) {
        this.dbFile = dbFile;
    }

    public LocalStorageModule(boolean production) {
        if (production) {
            System.setProperty("derby.stream.error.field", "java.lang.System.err");
        }
    }

    @Override
    protected void initialize() {
        bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("production");
        bindConstant().annotatedWith(DatabaseFilename.class).to(getDatabaseFile());
        bind(File.class).annotatedWith(DownloadDirectory.class)
                .toInstance(getDownloadsDir(System.getProperty("user.home")));
        bind(SchemaCreator.class).asEagerSingleton();


        bindDataSourceProviderType(DataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);

        addMapperClass(AccountMapper.class);
        addMapperClass(ItemMapper.class);
        addMapperClass(ProductMapper.class);
        addMapperClass(ArtistMapper.class);
        addMapperClass(ConfigurationMapper.class);
        addMapperClass(VersionMapper.class);
    }

    private File getDownloadsDir(String userHome) {
        File downloads = new File(userHome, "Downloads");
        if (!downloads.exists()) {
            if (!downloads.mkdirs()) {
                throw new AssertionError();
            }
        }
        return downloads;
    }

    private String getDatabaseFile() {
        if (null == dbFile) {
            String userHome = System.getProperty("user.home");
            return new File(userHome, ".mostlymusic.db").getAbsolutePath();
        } else {
            return dbFile.getAbsolutePath();
        }
    }

    public static class DataSourceProvider implements Provider<DataSource> {
        private final String fileName;

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
