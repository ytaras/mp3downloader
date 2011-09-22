package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.IItemsService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.dto.ItemsDto;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;
import com.mostlymusic.downloader.localdata.AccountMapper;
import com.mostlymusic.downloader.localdata.ItemsMapper;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class CheckServerUpdatesWorker extends SwingWorker<Void, CheckServerStatusStage> {

    private static final String METADATA_FETCHED_FORMAT = "Server has %d new items";
    private static final String ITEMS_FETCHED_FORMAT = "Fetched %d new items from server";
    private Account account;
    private IItemsService itemsService;
    private ApplicationModel applicationModel;
    private ItemsMapper itemsMapper;
    private AccountMapper accountMapper;


    @Inject
    public CheckServerUpdatesWorker(IItemsService itemsService, ApplicationModel applicationModel, ItemsMapper itemsMapper, AccountMapper accountMapper) {
        this.itemsService = itemsService;
        this.applicationModel = applicationModel;
        this.itemsMapper = itemsMapper;
        this.accountMapper = accountMapper;
    }


    @Override
    protected Void doInBackground() throws Exception {
        publish(new CheckServerStatusStage("Checking for updates from server", null));
        Long loadedLastOrderId = account.getLastOrderId();
        ItemsMetadataDto ordersMetadata = itemsService.getOrdersMetadata(loadedLastOrderId);

        LogEvent metadataFetchedLog = new LogEvent(String.format(METADATA_FETCHED_FORMAT, ordersMetadata.getTotalItems()));
        publish(new CheckServerStatusStage("Fetching list of tracks from server", metadataFetchedLog));
        if (0 == ordersMetadata.getTotalItems()) {
            // No updates
            return null;
        }
        int pageSize = 10;
        for (int i = 1; (i - 1) * pageSize < ordersMetadata.getTotalItems(); i++) {
            ItemsDto tracks = itemsService.getTracks(loadedLastOrderId, ordersMetadata.getLastItemId(), i, 10);
            LogEvent itemsFetchedLog = new LogEvent(String.format(ITEMS_FETCHED_FORMAT, tracks.getItems().size()));
            publish(new CheckServerStatusStage("Fetching list of tracks from server", itemsFetchedLog));

            for (Item item : tracks.getItems()) {
                itemsMapper.insertItem(item, account);
            }
        }
        account.setLastOrderId(ordersMetadata.getLastItemId());
        accountMapper.updateAccount(account);
        return null;
    }

    @Override
    protected void process(List<CheckServerStatusStage> checkServerStatusStages) {
        for (CheckServerStatusStage checkServerStatusStage : checkServerStatusStages) {
            if (checkServerStatusStage.getMessage() != null) {
                applicationModel.setStatus(checkServerStatusStage.getMessage());
            }
            applicationModel.publishLogStatus(checkServerStatusStage.getLogEvent());
        }
    }

    @Override
    protected void done() {
        applicationModel.setStatus(null);
        applicationModel.fireCheckServerDone();
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

    public void setAccount(Account account) {
        this.account = account;
    }
}

class CheckServerStatusStage {
    private LogEvent logEvent;
    private String message;

    CheckServerStatusStage(String message, LogEvent logEvent) {
        this.message = message;
        this.logEvent = logEvent;
    }

    public String getMessage() {
        return message;
    }

    public LogEvent getLogEvent() {
        return logEvent;
    }
}
