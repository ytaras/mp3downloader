package com.mostlymusic.downloader.client;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:56 AM
 */
public class OrdersMetadataDto {
    private int lastOrderId;
    private int count;

    public OrdersMetadataDto(int lastOrderId, int count) {
        this.lastOrderId = lastOrderId;
        this.count = count;
    }

    public int getLastOrderId() {
        return lastOrderId;
    }

    public void setLastOrderId(int lastOrderId) {
        this.lastOrderId = lastOrderId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdersMetadataDto that = (OrdersMetadataDto) o;

        if (count != that.count) return false;
        if (lastOrderId != that.lastOrderId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = lastOrderId;
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "OrdersMetadataDto{" +
                "lastOrderId=" + lastOrderId +
                ", count=" + count +
                '}';
    }
}
