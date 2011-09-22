package com.mostlymusic.downloader.gui;

import java.util.Date;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 2:15 PM
 */
public class LogEvent {
    private final String message;
    private final Date date;
    private final static String MESSAGE_FORMAT = "%1$tk:%1$tM:%1$tS - %2$s";

    public LogEvent(String message) {
        this.message = message;
        this.date = new Date();
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format(MESSAGE_FORMAT, date, message);
    }
}
