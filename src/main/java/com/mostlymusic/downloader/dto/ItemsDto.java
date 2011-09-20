package com.mostlymusic.downloader.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 5:05 PM
 */
public class ItemsDto {
    private List<ItemDto> items = new LinkedList<ItemDto>();
    private ItemsInfo info = new ItemsInfo();

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

        ItemsDto tracksDto = (ItemsDto) o;

        return !(items != null ? !items.equals(tracksDto.items) : tracksDto.items != null);

    }

    @Override
    public int hashCode() {
        return items != null ? items.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ItemsDto{" +
                "items=" + items +
                '}';
    }

    private static class ItemsInfo {
        private int pageCurrent;
        private int pageTotal;
        private int pageSize;
        private int totalRecords;
    }
}
