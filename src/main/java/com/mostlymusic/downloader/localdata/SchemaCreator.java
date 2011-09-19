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
    public SchemaCreator(DataSource dataSource, AccountMapper accountMapper) {
        this.dataSource = dataSource;
        this.accountMapper = accountMapper;
    }

    private DataSource dataSource;
    private AccountMapper accountMapper;

    public void createTables() throws SQLException {
        Connection connection = dataSource.getConnection();
        try {
            ResultSet tables = connection.getMetaData().getTables(null, null, AccountMapper.TABLE_NAME, null);
            if (!tables.next()) {
                accountMapper.createTable();
            }
        } finally {
            connection.close();
        }
    }
}
