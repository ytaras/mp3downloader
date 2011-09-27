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


    @Inject
    public SchemaCreator(DataSource dataSource, AccountMapper accountMapper, ItemMapper itemMapper,
                         ProductMapper productMapper, ArtistMapper artistMapper, ConfigurationMapper configurationMapper) {
        this.dataSource = dataSource;
        this.accountMapper = accountMapper;
        this.itemMapper = itemMapper;
        this.productMapper = productMapper;
        this.artistMapper = artistMapper;
        this.configurationMapper = configurationMapper;
    }

    private final DataSource dataSource;
    private final AccountMapper accountMapper;
    private final ItemMapper itemMapper;
    private final ProductMapper productMapper;
    private final ArtistMapper artistMapper;
    private final ConfigurationMapper configurationMapper;

    public void createTables() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
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

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, tableName, null);
        return tables.next();
    }
}
