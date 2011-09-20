package com.mostlymusic.downloader.client;

import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 5:57 PM
 */
public interface IAuthService {
    String USERNAME = "login[username]";
    String PASSWORD = "login[password]";

    boolean auth(String name, String pass) throws IOException;
}
