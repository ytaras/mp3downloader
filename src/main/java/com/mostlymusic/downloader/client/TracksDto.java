package com.mostlymusic.downloader.client;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 5:05 PM
 */
public class TracksDto {
    private String field;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "TracksDto{" +
                "field='" + field + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TracksDto tracksDto = (TracksDto) o;

        return !(field != null ? !field.equals(tracksDto.field) : tracksDto.field != null);

    }

    @Override
    public int hashCode() {
        return field != null ? field.hashCode() : 0;
    }
}
