package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 6:37 PM
 */
class InternalServerErrrorException extends RequestException {
    public InternalServerErrrorException(String message) {
        super(message);
    }
}
