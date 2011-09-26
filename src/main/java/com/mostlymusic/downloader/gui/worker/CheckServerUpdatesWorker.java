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
import com.mostlymusic.downloader.localdata.ItemMapper;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class CheckServerUpdatesWorker extends AbstractSwingClientWorker<Void, CheckServerStatusStage> {

    private static final String METADATA_FETCHED_FORMAT = "Server has %d new items";
    private static final String ITEMS_FETCHED_FORMAT = "Fetched %d new items from server";
    private Account account;
    private IItemsService itemsService;
    private ItemMapper itemMapper;
    private AccountMapper accountMapper;


    @Inject
    public CheckServerUpdatesWorker(IItemsService itemsService, ApplicationModel applicationModel, ItemMapper itemMapper, AccountMapper accountMapper) {
        super(applicationModel);
        this.itemsService = itemsService;
        this.itemMapper = itemMapper;
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
                itemMapper.insertItem(item, account);
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
                getApplicationModel().setStatus(checkServerStatusStage.getMessage());
            }
            getApplicationModel().publishLogStatus(checkServerStatusStage.getLogEvent());
        }
    }

    @Override
    protected void beforeGet() {
        getApplicationModel().setStatus(null);
        getApplicationModel().fireCheckServerDone();
    }

    @Override
    protected void doDone(Void aVoid) {
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
