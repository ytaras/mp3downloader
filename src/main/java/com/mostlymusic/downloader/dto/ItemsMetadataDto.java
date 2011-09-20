package com.mostlymusic.downloader.dto;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:56 AM
 */
public class ItemsMetadataDto {
    private long lastItemId;
    private int totalItems;

    public ItemsMetadataDto(long lastItemId, int totalitems) {
        this.lastItemId = lastItemId;
        this.totalItems = totalitems;
    }

    public long getLastItemId() {
        return lastItemId;
    }

    public void setLastItemId(int lastItemId) {
        this.lastItemId = lastItemId;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemsMetadataDto that = (ItemsMetadataDto) o;

        return totalItems == that.totalItems && lastItemId == that.lastItemId;

    }

    @Override
    public int hashCode() {
        int result = (int) (lastItemId ^ (lastItemId >>> 32));
        result = 31 * result + totalItems;
        return result;
    }

    @Override
    public String toString() {
        return "ItemsMetadataDto{" +
                "lastItemId=" + lastItemId +
                ", totalItems=" + totalItems +
                '}';
    }
}
