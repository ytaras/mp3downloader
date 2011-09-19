package com.mostlymusic.downloader.localdata;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 6:17 PM
 */
public class ConnectionManager implements IConnectionManager {

    private DataSource dataSource;

    @Inject
    public ConnectionManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void initDatabase() throws SQLException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            ResultSet tables = connection.getMetaData().getTables(null, null, null, null);
            boolean accountsFound = false;
            while (tables.next()) {
                if (tables.getString("TABLE_NAME").equalsIgnoreCase("ACCOUNTS")) {
                    accountsFound = true;
                }
            }
            if (!accountsFound) {
                connection.prepareStatement("CREATE TABLE ACCOUNTS (ID int)").execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }

    }
}
