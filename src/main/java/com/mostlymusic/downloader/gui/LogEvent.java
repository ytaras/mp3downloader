package com.mostlymusic.downloader.gui;

import java.util.Date;

import org.jetbrains.annotations.Nullable;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 2:15 PM
 */
public class LogEvent {
    private final String message;
    private final Date date;
    private final Throwable exception;
    private final static String MESSAGE_FORMAT = "%1$tk:%1$tM:%1$tS - %2$s";


    public LogEvent(String message) {
        this(message, null);
    }

    public LogEvent(String message, @Nullable Throwable cause) {
        this.message = message;
        this.exception = cause;
        this.date = new Date();
    }

    public Throwable getException() {
        return exception;
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
