package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;

/**
 * @author ytaras
 *         Date: 9/21/11
 *         Time: 9:11 AM
 */
public class MetadataFetchEvent {
    private ItemsMetadataDto itemsMetadataDto;
    private Account account;
    private Exception exception;

    public MetadataFetchEvent(ItemsMetadataDto itemsMetadataDto, Account account) {
        this.itemsMetadataDto = itemsMetadataDto;
        this.account = account;
    }

    public MetadataFetchEvent(Exception exception) {
        this.exception = exception;
    }

    public boolean isSuccessful() {
        return exception == null;
    }

    public ItemsMetadataDto getItemsMetadataDto() {
        return itemsMetadataDto;
    }

    public Account getAccount() {
        return account;
    }

    public Exception getException() {
        return exception;
    }
}
