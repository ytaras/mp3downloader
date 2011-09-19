package com.mostlymusic.downloader.localdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 6:17 PM
 */
public class ConnectionManager implements IConnectionManager {
    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby:" + System.getProperty("user.home") +
                    "/.mostlymusic.db;create=true");
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
            return connection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
