package com.mostlymusic.downloader.localdata;

import java.sql.Connection;

/**
 * TODO Make sure it is compatible with Guice
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 6:17 PM
 */
public interface IConnectionManager {
    /**
     * If necessary, should create local database and according scheme
     * @return Connection to local database
     */
    Connection getConnection();
}
