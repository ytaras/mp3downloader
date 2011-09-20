package com.mostlymusic.downloader.gui;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 1:29 PM
 */
public interface ApplicationModelListener {
    void statusUnset();

    void statusSet(String status);
}
