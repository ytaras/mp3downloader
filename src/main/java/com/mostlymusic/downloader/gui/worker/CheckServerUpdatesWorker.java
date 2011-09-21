package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.IItemsService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import com.mostlymusic.downloader.gui.ApplicationModel;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class CheckServerUpdatesWorker extends SwingWorker<Void, CheckServerStatusStage> {

    private final Account account;
    private IItemsService itemsService;
    private ApplicationModel applicationModel;

    @Inject
    public CheckServerUpdatesWorker(Account account, IItemsService itemsService, ApplicationModel applicationModel) {
        this.account = account;
        this.itemsService = itemsService;
        this.applicationModel = applicationModel;
    }


    @Override
    protected Void doInBackground() throws Exception {
        publish(CheckServerStatusStage.METADATA_FETCHING);
        ItemsMetadataDto ordersMetadata = itemsService.getOrdersMetadata(account.getLastOrderId());
        publish(CheckServerStatusStage.METADATA_FETCHED);
        if (0 == ordersMetadata.getTotalItems()) {
            // No updates
            return null;
        }
        return null;
    }

    @Override
    protected void process(List<CheckServerStatusStage> checkServerStatusStages) {
        for (CheckServerStatusStage checkServerStatusStage : checkServerStatusStages) {
            applicationModel.setStatus(checkServerStatusStage.getMessage());
        }
    }

    @Override
    protected void done() {
        applicationModel.setStatus(null);
        try {
            get();
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

enum CheckServerStatusStage {
    METADATA_FETCHING("Checking list of updates from server"), METADATA_FETCHED("Fetched list of updates");

    CheckServerStatusStage(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }
}
