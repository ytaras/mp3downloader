package com.mostlymusic.downloader.client;

import java.io.IOException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 5:57 PM
 */
public interface IAuthService {
    boolean auth(String name, String pass) throws IOException;
}
