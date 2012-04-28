package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.Artist;
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
import com.mostlymusic.downloader.manager.AccountManager;
import com.mostlymusic.downloader.manager.AccountMapper;
import com.mostlymusic.downloader.manager.ArtistMapper;
import com.mostlymusic.downloader.manager.ConfigurationMapper;
import com.mostlymusic.downloader.manager.ItemManager;
import com.mostlymusic.downloader.manager.ProductMapper;

import java.util.List;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class CheckServerUpdatesWorker extends AbstractSwingClientWorker<Void, CheckServerStatusStage> {

    private static final String METADATA_FETCHED_FORMAT = "Server has %d new items";
    private static final String ITEMS_FETCHED_FORMAT = "Fetched %d new items from server";
    private final ItemsService itemsService;
    private final ProductsService productsService;
    private final ProductMapper productMapper;
    private final AccountMapper accountMapper;
    private final ArtistsService artistsService;
    private final ArtistMapper artistMapper;
    private final ConfigurationMapper configurationMapper;
    private final FileDownloader fileDownloader;
    private final ItemManager itemManager;
    private final AccountManager accountManager;


    @Inject
    public CheckServerUpdatesWorker(ItemsService itemsService, ApplicationModel applicationModel,
                                    AccountMapper accountMapper, ProductMapper productMapper, ProductsService productsService,
                                    ArtistsService artistsService, ArtistMapper artistMapper,
                                    ConfigurationMapper configurationMapper, FileDownloader fileDownloader,
                                    ItemManager itemManager, AccountManager accountManager) {
        super(applicationModel);
        this.itemsService = itemsService;
        this.accountMapper = accountMapper;
        this.productMapper = productMapper;
        this.productsService = productsService;
        this.artistsService = artistsService;
        this.artistMapper = artistMapper;
        this.configurationMapper = configurationMapper;
        this.fileDownloader = fileDownloader;
        this.itemManager = itemManager;
        this.accountManager = accountManager;
    }


    @Override
    protected Void doInBackground() throws Exception {
        publish(new CheckServerStatusStage(null));
        Account account = accountManager.getCurrentAccount();
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
                    itemManager.saveItem(item);
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

        while (!artistMapper.findUnknownArtists().isEmpty()) {
            List<Long> unknownArtists = artistMapper.findUnknownArtists();
            LogEvent artistsToFetchLog = new LogEvent(String.format("Fetching new %d artists from server", unknownArtists.size()));
            publish(new CheckServerStatusStage(artistsToFetchLog));
            List<Artist> products = artistsService.getArtists(unknownArtists);
            for (Artist product : products) {
                artistMapper.insertArtist(product);
            }
        }

        if (configurationMapper.getAutoDownload()) {
            for (Item item : itemManager.findItemByStatus(Item.AVAILABLE)) {
                fileDownloader.scheduleDownload(item);
            }
        }
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
}

class CheckServerStatusStage {
    private final LogEvent logEvent;

    CheckServerStatusStage(LogEvent logEvent) {
        this.logEvent = logEvent;
    }

    public LogEvent getLogEvent() {
        return logEvent;
    }
}
