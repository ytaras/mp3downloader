package com.mostlymusic.downloader.client;

import java.io.IOException;

import com.mostlymusic.downloader.client.exceptions.RequestException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 5:57 PM
 */
public interface AuthService {
    String USERNAME = "login[username]";
    String PASSWORD = "login[password]";

    boolean auth(String name, String pass) throws IOException, RequestException;
}
