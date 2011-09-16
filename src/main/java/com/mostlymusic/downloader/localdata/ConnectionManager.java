package com.mostlymusic.downloader.localdata;

import java.sql.Connection;
import java.sql.DriverManager;
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
            return DriverManager.getConnection("jdbc:derby:" + System.getProperty("user.home") + "/.mostlymusic.db;create=true");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
