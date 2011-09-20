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

    public ItemsInfo getInfo() {
        return info;
    }

    public static class ItemsInfo {
        private int pageCurrent;
        private int pageTotal;
        private int pageSize;
        private int totalRecords;

        public int getPageCurrent() {
            return pageCurrent;
        }

        public void setPageCurrent(int pageCurrent) {
            this.pageCurrent = pageCurrent;
        }

        public int getPageTotal() {
            return pageTotal;
        }

        public void setPageTotal(int pageTotal) {
            this.pageTotal = pageTotal;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalRecords() {
            return totalRecords;
        }

        public void setTotalRecords(int totalRecords) {
            this.totalRecords = totalRecords;
        }
    }
}
