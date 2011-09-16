package com.mostlymusic.downloader.client;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 5:05 PM
 */
public class TracksDto {
    private List<TrackDto> tracks;

    public List<TrackDto> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDto> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TracksDto tracksDto = (TracksDto) o;

        return !(tracks != null ? !tracks.equals(tracksDto.tracks) : tracksDto.tracks != null);

    }

    @Override
    public int hashCode() {
        return tracks != null ? tracks.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TracksDto{" +
                "tracks=" + tracks +
                '}';
    }
}
