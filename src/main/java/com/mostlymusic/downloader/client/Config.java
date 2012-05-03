package com.mostlymusic.downloader.client;

/**
 * Created with IntelliJ IDEA.
 * User: anjey
 * Date: 28.04.12
 * Time: 9:42
 * To change this template use File | Settings | File Templates.
 */
public class Config {
    private int maxPageSize;

    public Config() {
    }

    public Config(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }
}

