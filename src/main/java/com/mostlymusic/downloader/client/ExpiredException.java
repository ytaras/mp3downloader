package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 6:35 PM
 */
public class ExpiredException extends RequestException {
    public ExpiredException(String message) {
        super(message);
    }
}
