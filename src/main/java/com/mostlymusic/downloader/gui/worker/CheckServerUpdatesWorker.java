package com.mostlymusic.downloader.gui.worker;

import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import com.mostlymusic.downloader.gui.ApplicationModel;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class CheckServerUpdatesWorker extends SwingWorker<ItemsMetadataDto, Void> {

    private final Account account;
    private ItemsService itemsService;
    private ApplicationModel applicationModel;

    public CheckServerUpdatesWorker(Account account, ItemsService itemsService, ApplicationModel applicationModel) {
        this.account = account;
        this.itemsService = itemsService;
        this.applicationModel = applicationModel;
    }


    @Override
    protected ItemsMetadataDto doInBackground() throws Exception {
        if (account.getLastOrderId() == null) {
            return itemsService.getOrdersMetadata();
        } else {
            return itemsService.getOrdersMetadata(account.getLastOrderId());
        }
    }

    @Override
    protected void done() {
        try {
            ItemsMetadataDto itemsMetadataDto = get();
            applicationModel.fireMetadataFetchedEvent(itemsMetadataDto, account);
        } catch (InterruptedException e) {
            handleException(e);
        } catch (ExecutionException e) {
            handleException(e);
        }
    }

    private void handleException(Exception e) {
        applicationModel.setStatus(null);
        applicationModel.fireExceptionEvent(e);
    }
}
