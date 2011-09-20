package com.mostlymusic.downloader.dto;

import java.util.Date;

/**
 * @author ytaras
 *         Date: 9/16/11
 *         Time: 5:39 PM
 */
public class ItemDto {
    private long itemId;
    private long productId;
    private String linkHash;
    private int downloadsBought;
    private int downloadsUsed;
    private long linkId;
    private String linkTitle;
    private String status;
    private String fileName;
    private Date createdAt;
    private Date updatedAt;


    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getLinkHash() {
        return linkHash;
    }

    public void setLinkHash(String linkHash) {
        this.linkHash = linkHash;
    }

    public int getDownloadsBought() {
        return downloadsBought;
    }

    public void setDownloadsBought(int downloadsBought) {
        this.downloadsBought = downloadsBought;
    }

    public int getDownloadsUsed() {
        return downloadsUsed;
    }

    public void setDownloadsUsed(int downloadsUsed) {
        this.downloadsUsed = downloadsUsed;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemDto itemDto = (ItemDto) o;

        if (downloadsBought != itemDto.downloadsBought) return false;
        if (downloadsUsed != itemDto.downloadsUsed) return false;
        if (itemId != itemDto.itemId) return false;
        if (linkId != itemDto.linkId) return false;
        if (productId != itemDto.productId) return false;
        if (createdAt != null ? !createdAt.equals(itemDto.createdAt) : itemDto.createdAt != null) return false;
        if (fileName != null ? !fileName.equals(itemDto.fileName) : itemDto.fileName != null) return false;
        if (linkHash != null ? !linkHash.equals(itemDto.linkHash) : itemDto.linkHash != null) return false;
        if (linkTitle != null ? !linkTitle.equals(itemDto.linkTitle) : itemDto.linkTitle != null) return false;
        if (status != null ? !status.equals(itemDto.status) : itemDto.status != null) return false;
        if (updatedAt != null ? !updatedAt.equals(itemDto.updatedAt) : itemDto.updatedAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (itemId ^ (itemId >>> 32));
        result = 31 * result + (int) (productId ^ (productId >>> 32));
        result = 31 * result + (linkHash != null ? linkHash.hashCode() : 0);
        result = 31 * result + downloadsBought;
        result = 31 * result + downloadsUsed;
        result = 31 * result + (int) (linkId ^ (linkId >>> 32));
        result = 31 * result + (linkTitle != null ? linkTitle.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (fileName != null ? fileName.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ItemDto{" +
                "itemId=" + itemId +
                ", productId=" + productId +
                ", linkHash='" + linkHash + '\'' +
                ", downloadsBought=" + downloadsBought +
                ", downloadsUsed=" + downloadsUsed +
                ", linkId=" + linkId +
                ", linkTitle='" + linkTitle + '\'' +
                ", status='" + status + '\'' +
                ", fileName='" + fileName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
