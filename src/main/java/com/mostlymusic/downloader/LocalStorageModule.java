package com.mostlymusic.downloader;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.mostlymusic.downloader.localdata.AccountMapper;
import com.mostlymusic.downloader.localdata.ItemsMapper;
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
    @Override
    protected void initialize() {
        bindConstant().annotatedWith(Names.named("mybatis.environment.id")).to("production");
        bindConstant().annotatedWith(DatabaseFilename.class).to(getDatabaseFile());

        bindDataSourceProviderType(DataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);

        addMapperClass(AccountMapper.class);
        addMapperClass(ItemsMapper.class);
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
