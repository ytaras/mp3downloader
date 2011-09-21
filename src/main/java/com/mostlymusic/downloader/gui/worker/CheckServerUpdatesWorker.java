package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.IItemsService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.dto.ItemsDto;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import com.mostlymusic.downloader.gui.ApplicationModel;
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

    private final Account account;
    private IItemsService itemsService;
    private ApplicationModel applicationModel;
    private ItemsMapper itemsMapper;
    private AccountMapper accountMapper;

    @Inject
    public CheckServerUpdatesWorker(Account account, IItemsService itemsService, ApplicationModel applicationModel, ItemsMapper itemsMapper, AccountMapper accountMapper) {
        this.account = account;
        this.itemsService = itemsService;
        this.applicationModel = applicationModel;
        this.itemsMapper = itemsMapper;
        this.accountMapper = accountMapper;
    }


    @Override
    protected Void doInBackground() throws Exception {
        publish(CheckServerStatusStage.METADATA_FETCHING);
        ItemsMetadataDto ordersMetadata = itemsService.getOrdersMetadata(account.getLastOrderId());
        if (0 == ordersMetadata.getTotalItems()) {
            // No updates
            return null;
        }
        account.setLastOrderId(ordersMetadata.getLastItemId());
        accountMapper.updateAccount(account);
        publish(CheckServerStatusStage.FETCH_ITEMS);
        int pageSize = 10;
        for (int i = 1; i * pageSize < ordersMetadata.getTotalItems(); i++) {
            ItemsDto tracks = itemsService.getTracks(account.getLastOrderId(), ordersMetadata.getLastItemId(), i, 10);
            for (Item item : tracks.getItems()) {
                itemsMapper.insertItem(item, account);
            }
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
    METADATA_FETCHING("Checking for updates from server"),
    FETCH_ITEMS("Fetching list of tracks from server");

    CheckServerStatusStage(String message) {
        this.message = message;
    }

    private String message;

    public String getMessage() {
        return message;
    }
}
