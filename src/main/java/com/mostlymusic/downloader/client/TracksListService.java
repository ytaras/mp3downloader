package com.mostlymusic.downloader.client;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 3:59 PM
 */
public class TracksListService implements ITracksListService {
    private String serviceUrl;

    public TracksDto getTracks() {
        return new TracksDto();
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
