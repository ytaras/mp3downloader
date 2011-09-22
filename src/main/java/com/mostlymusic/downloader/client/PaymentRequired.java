package com.mostlymusic.downloader.client;

import com.mostlymusic.downloader.client.exceptions.RequestException;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 6:36 PM
 */
public class PaymentRequired extends RequestException {
    public PaymentRequired(String message) {
        super(message);
    }
}
