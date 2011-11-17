package com.mostlymusic.downloader.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 5:05 PM
 */
public class ItemsDto {
    private List<Item> items = new LinkedList<Item>();
    private ItemsInfo info = new ItemsInfo();

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    @SuppressWarnings({"RedundantIfStatement"})
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemsDto itemsDto = (ItemsDto) o;

        if (info != null ? !info.equals(itemsDto.info) : itemsDto.info != null) return false;
        if (items != null ? !items.equals(itemsDto.items) : itemsDto.items != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = items != null ? items.hashCode() : 0;
        result = 31 * result + (info != null ? info.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ItemsDto{" +
                "items=" + items +
                ", info=" + info +
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

        @SuppressWarnings({"RedundantIfStatement"})
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ItemsInfo itemsInfo = (ItemsInfo) o;

            if (pageCurrent != itemsInfo.pageCurrent) return false;
            if (pageSize != itemsInfo.pageSize) return false;
            if (pageTotal != itemsInfo.pageTotal) return false;
            if (totalRecords != itemsInfo.totalRecords) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = pageCurrent;
            result = 31 * result + pageTotal;
            result = 31 * result + pageSize;
            result = 31 * result + totalRecords;
            return result;
        }

        @Override
        public String toString() {
            return "ItemsInfo{" +
                    "pageCurrent=" + pageCurrent +
                    ", pageTotal=" + pageTotal +
                    ", pageSize=" + pageSize +
                    ", totalRecords=" + totalRecords +
                    '}';
        }
    }
}
