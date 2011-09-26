package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.client.Product;
import com.mostlymusic.downloader.client.ProductsService;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.dto.ItemsDto;
import com.mostlymusic.downloader.dto.ItemsMetadataDto;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;
import com.mostlymusic.downloader.localdata.AccountMapper;
import com.mostlymusic.downloader.localdata.ItemMapper;
import com.mostlymusic.downloader.localdata.ProductMapper;

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
    private ItemsService itemsService;
    private ItemMapper itemMapper;
    private ProductMapper productMapper;
    private ProductsService productsService;
    private AccountMapper accountMapper;


    @Inject
    public CheckServerUpdatesWorker(ItemsService itemsService, ApplicationModel applicationModel, ItemMapper itemMapper,
                                    AccountMapper accountMapper, ProductMapper productMapper, ProductsService productsService) {
        super(applicationModel);
        this.itemsService = itemsService;
        this.itemMapper = itemMapper;
        this.accountMapper = accountMapper;
        this.productMapper = productMapper;
        this.productsService = productsService;
    }


    @Override
    protected Void doInBackground() throws Exception {
        publish(new CheckServerStatusStage("Checking for updates from server", null));
        Long loadedLastOrderId = account.getLastOrderId();
        ItemsMetadataDto ordersMetadata = itemsService.getOrdersMetadata(loadedLastOrderId);

        LogEvent metadataFetchedLog = new LogEvent(String.format(METADATA_FETCHED_FORMAT, ordersMetadata.getTotalItems()));
        publish(new CheckServerStatusStage("Fetching list of tracks from server", metadataFetchedLog));
        if (0 != ordersMetadata.getTotalItems()) {
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
        }

        while (!productMapper.findUnknownProducts().isEmpty()) {
            List<Long> unknownProducts = productMapper.findUnknownProducts();
            LogEvent itemsFetchedLog = new LogEvent(String.format("Fetching new %d products from server", unknownProducts.size()));
            publish(new CheckServerStatusStage("Fetching products from server", itemsFetchedLog));
            List<Product> products = productsService.getProducts(unknownProducts);
            for (Product product : products) {
                productMapper.insertProduct(product);
            }
        }
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
