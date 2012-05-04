package com.mostlymusic.downloader.manager;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.google.inject.Inject;
import com.mostlymusic.downloader.DownloadDirectory;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 5:19 PM
 */
public class SchemaCreator {

    public static int CURRENT_APP_VERSION = 3;
    private final File defaultDownloadPath;

    @Inject
    public SchemaCreator(DataSource dataSource, AccountMapper accountMapper, ItemMapper itemMapper,
                         ProductMapper productMapper, ArtistMapper artistMapper, ConfigurationMapper configurationMapper,
                         VersionMapper versionMapper, @DownloadDirectory File defaultDownloadPath)
            throws SQLException {
        this.dataSource = dataSource;
        this.accountMapper = accountMapper;
        this.itemMapper = itemMapper;
        this.productMapper = productMapper;
        this.artistMapper = artistMapper;
        this.configurationMapper = configurationMapper;
        this.versionMapper = versionMapper;
        this.defaultDownloadPath = defaultDownloadPath;
        createTables();
    }

    private final DataSource dataSource;
    private final AccountMapper accountMapper;
    private final ItemMapper itemMapper;
    private final ProductMapper productMapper;
    private final ArtistMapper artistMapper;
    private final ConfigurationMapper configurationMapper;
    private final VersionMapper versionMapper;

    public void createTables() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            int currentVersion;
            if (!tableExists(connection, VersionMapper.TABLE_NAME)) {
                dropTable(connection, AccountMapper.TABLE_NAME);
                dropTable(connection, ItemMapper.TABLE_NAME);
                dropTable(connection, ProductMapper.TABLE_NAME);
                dropTable(connection, ArtistMapper.TABLE_NAME);
                dropTable(connection, ConfigurationMapper.TABLE_NAME);
                versionMapper.createSchema();
                versionMapper.createInitialConfig();
                currentVersion = 0;
            } else {
                currentVersion = versionMapper.loadVersion();
            }
            for (int i = currentVersion + 1; i <= CURRENT_APP_VERSION; i++) {
                migrateTo(i);
            }
        } finally {
            connection.close();
        }
    }

    public void migrateTo1() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            if (!tableExists(connection, VersionMapper.TABLE_NAME)) {
                dropTable(connection, AccountMapper.TABLE_NAME);
                dropTable(connection, ItemMapper.TABLE_NAME);
                dropTable(connection, ProductMapper.TABLE_NAME);
                dropTable(connection, ArtistMapper.TABLE_NAME);
                dropTable(connection, ConfigurationMapper.TABLE_NAME);
                versionMapper.createSchema();
                versionMapper.createInitialConfig();
            }
            if (!tableExists(connection, AccountMapper.TABLE_NAME)) {
                accountMapper.createSchema();
            }
            if (!tableExists(connection, ItemMapper.TABLE_NAME)) {
                itemMapper.createSchema();
            }
            if (!tableExists(connection, ProductMapper.TABLE_NAME)) {
                productMapper.createSchema();
            }
            if (!tableExists(connection, ArtistMapper.TABLE_NAME)) {
                artistMapper.createSchema();
            }
            if (!tableExists(connection, ConfigurationMapper.TABLE_NAME)) {
                configurationMapper.createSchema();
                configurationMapper.insertConfig(defaultDownloadPath.getAbsolutePath());
            }
        } finally {
            connection.close();
        }
    }

    private void dropTable(Connection connection, String tableName) throws SQLException {
        if (tableExists(connection, tableName)) {
            connection.prepareStatement("DROP TABLE " + tableName).execute();
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, tableName, null);
        return tables.next();
    }

    public void migrateTo(int version) throws SQLException {
        switch (version) {
            case 1:
                migrateTo1();
                break;
            case 2:
                migrateTo2();
                break;
            case 3: 
                migrateTo3();
                break;
            default:
                throw new IllegalArgumentException("Unknown version " + version);
        }
        versionMapper.setVersion(version);
    }

    private void migrateTo3() {
        configurationMapper.toVersion3();
    }

    private void migrateTo2() {
        // TODO Find out how to do multiple queries in MyBatis
        configurationMapper.toVersion2_1();
        configurationMapper.toVersion2_2();
    }
}
