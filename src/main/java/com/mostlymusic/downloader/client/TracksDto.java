package com.mostlymusic.downloader.client;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 5:05 PM
 */
public class TracksDto {
    private List<ItemDto> items;

    public List<ItemDto> getItems() {
        return items;
    }

    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TracksDto tracksDto = (TracksDto) o;

        return !(items != null ? !items.equals(tracksDto.items) : tracksDto.items != null);

    }

    @Override
    public int hashCode() {
        return items != null ? items.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "TracksDto{" +
                "items=" + items +
                '}';
    }
}
