package com.mostlymusic.downloader.client;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 5:39 PM
 */
public class TrackDto {
    private int id;
    private String name;
    private String artist;
    private String album;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrackDto trackDto = (TrackDto) o;

        return id == trackDto.id && !(album != null ? !album.equals(trackDto.album) : trackDto.album != null) &&
                !(artist != null ? !artist.equals(trackDto.artist) : trackDto.artist != null) && !(name != null ?
                !name.equals(trackDto.name) : trackDto.name != null);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (artist != null ? artist.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TrackDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}
