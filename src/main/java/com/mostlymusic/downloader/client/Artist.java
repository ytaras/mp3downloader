package com.mostlymusic.downloader.client;

/**
 * @author ytaras
 *         Date: 9/26/11
 *         Time: 3:14 PM
 */
public class Artist {
    private long artistId;
    private String name;

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Artist artist = (Artist) o;

        if (artistId != artist.artistId) return false;
        if (name != null ? !name.equals(artist.name) : artist.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (artistId ^ (artistId >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", name='" + name + '\'' +
                '}';
    }
}
