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
    public SchemaCreator(DataSource dataSource, AccountMapper accountMapper, ItemMapper itemMapper, ProductMapper productMapper) {
        this.dataSource = dataSource;
        this.accountMapper = accountMapper;
        this.itemMapper = itemMapper;
        this.productMapper = productMapper;
    }

    private DataSource dataSource;
    private AccountMapper accountMapper;
    private ItemMapper itemMapper;
    private ProductMapper productMapper;

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
        } finally {
            connection.close();
        }
    }

    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        ResultSet tables = connection.getMetaData().getTables(null, null, tableName, null);
        return tables.next();
    }
}
