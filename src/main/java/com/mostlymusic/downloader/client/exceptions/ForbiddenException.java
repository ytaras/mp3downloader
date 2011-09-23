package com.mostlymusic.downloader.client.exceptions;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 6:34 PM
 */
public class ForbiddenException extends RequestException {
    public ForbiddenException(String message) {
        super(message);
    }
}
