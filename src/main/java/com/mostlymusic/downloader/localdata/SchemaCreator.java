package com.mostlymusic.downloader.localdata;

import com.google.inject.Inject;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 5:19 PM
 */
public class SchemaCreator {


    private static final String VERSION_TABLE_NAME = "DOWNLOADER_VERSION";

    @Inject
    public SchemaCreator(DataSource dataSource, AccountMapper accountMapper, ItemMapper itemMapper,
                         ProductMapper productMapper, ArtistMapper artistMapper, ConfigurationMapper configurationMapper,
                         VersionMapper versionMapper)
            throws SQLException {
        this.dataSource = dataSource;
        this.accountMapper = accountMapper;
        this.itemMapper = itemMapper;
        this.productMapper = productMapper;
        this.artistMapper = artistMapper;
        this.configurationMapper = configurationMapper;
        this.versionMapper = versionMapper;
    }

    private final DataSource dataSource;
    private final AccountMapper accountMapper;
    private final ItemMapper itemMapper;
    private final ProductMapper productMapper;
    private final ArtistMapper artistMapper;
    private final ConfigurationMapper configurationMapper;
    private final VersionMapper versionMapper;

    @Inject
    public void createTables() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            if (!tableExists(connection, VERSION_TABLE_NAME)) {
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
                configurationMapper.insertConfig();
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
}
