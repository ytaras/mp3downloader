package com.mostlymusic.downloader.client;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 11:56 AM
 */
public class OrdersMetadataDto {
    private long lastOrderId;
    private int count;

    public OrdersMetadataDto(long lastOrderId, int count) {
        this.lastOrderId = lastOrderId;
        this.count = count;
    }

    public long getLastOrderId() {
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

        return count == that.count && lastOrderId == that.lastOrderId;

    }

    @Override
    public int hashCode() {
        int result = (int) (lastOrderId ^ (lastOrderId >>> 32));
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
