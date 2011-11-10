package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.ArtistsService;
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
import com.mostlymusic.downloader.localdata.ArtistMapper;
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
    private final ItemsService itemsService;
    private final ItemMapper itemMapper;
    private final ProductsService productsService;
    private final ProductMapper productMapper;
    private final AccountMapper accountMapper;
    private final ArtistsService artistsService;
    private final ArtistMapper artistMapper;


    @Inject
    public CheckServerUpdatesWorker(ItemsService itemsService, ApplicationModel applicationModel, ItemMapper itemMapper,
                                    AccountMapper accountMapper, ProductMapper productMapper, ProductsService productsService,
                                    ArtistsService artistsService, ArtistMapper artistMapper) {
        super(applicationModel);
        this.itemsService = itemsService;
        this.itemMapper = itemMapper;
        this.accountMapper = accountMapper;
        this.productMapper = productMapper;
        this.productsService = productsService;
        this.artistsService = artistsService;
        this.artistMapper = artistMapper;
    }


    @Override
    protected Void doInBackground() throws Exception {
        publish(new CheckServerStatusStage(null));
        // TODO NPE is thrown here
        Long loadedLastOrderId = account.getLastOrderId();
        ItemsMetadataDto ordersMetadata = itemsService.getOrdersMetadata(loadedLastOrderId);

        LogEvent metadataFetchedLog = new LogEvent(String.format(METADATA_FETCHED_FORMAT, ordersMetadata.getTotalItems()));
        publish(new CheckServerStatusStage(metadataFetchedLog));
        if (0 != ordersMetadata.getTotalItems()) {
            int pageSize = 10;
            for (int i = 1; (i - 1) * pageSize < ordersMetadata.getTotalItems(); i++) {
                ItemsDto tracks = itemsService.getTracks(loadedLastOrderId, ordersMetadata.getLastItemId(), i, 10);
                LogEvent itemsFetchedLog = new LogEvent(String.format(ITEMS_FETCHED_FORMAT, tracks.getItems().size()));
                publish(new CheckServerStatusStage(itemsFetchedLog));

                for (Item item : tracks.getItems()) {
                    if (itemMapper.contains(item.getItemId())) {
                        itemMapper.updateItem(item, account);
                    } else {
                        itemMapper.insertItem(item, account);
                    }
                }
            }
            account.setLastOrderId(ordersMetadata.getLastItemId());
            accountMapper.updateAccount(account);
        }

        while (!productMapper.findUnknownProducts().isEmpty()) {
            List<Long> unknownProducts = productMapper.findUnknownProducts();
            LogEvent productToFetchLog = new LogEvent(String.format("Fetching new %d products from server", unknownProducts.size()));
            publish(new CheckServerStatusStage(productToFetchLog));
            List<Product> products = productsService.getProducts(unknownProducts);
            for (Product product : products) {
                productMapper.insertProduct(product);
            }
        }

        // TODO Enable once this will be fixed
        /*
        while (false || !artistMapper.findUnknownArtists().isEmpty()) {
            List<Long> unknownArtists = artistMapper.findUnknownArtists();
            LogEvent productToFetchLog = new LogEvent(String.format("Fetching new %d artists from server", unknownArtists.size()));
            publish(new CheckServerStatusStage("Fetching artists from server", productToFetchLog));
            List<Artist> products = artistsService.getArtists(unknownArtists);
            for (Artist product : products) {
                artistMapper.insertArtist(product);
            }
        } */

        return null;
    }

    @Override
    protected void process(List<CheckServerStatusStage> checkServerStatusStages) {
        for (CheckServerStatusStage checkServerStatusStage : checkServerStatusStages) {
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

    CheckServerStatusStage(LogEvent logEvent) {
        this.logEvent = logEvent;
    }

    public LogEvent getLogEvent() {
        return logEvent;
    }
}
