package com.mostlymusic.downloader.gui.worker;

import java.io.IOException;
import java.util.List;

import com.google.inject.Inject;
import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.client.ArtistsService;
import com.mostlymusic.downloader.client.Config;
import com.mostlymusic.downloader.client.ConfigService;
import com.mostlymusic.downloader.client.ItemsService;
import com.mostlymusic.downloader.client.Product;
import com.mostlymusic.downloader.client.ProductsService;
import com.mostlymusic.downloader.client.exceptions.RequestException;
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

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 6:44 PM
 */
public class CheckServerUpdatesWorker extends AbstractSwingClientWorker<Void, CheckServerStatusStage> {

    private static final String METADATA_FETCHED_FORMAT = "Server has %d new items";
    private static final String ITEMS_FETCHED_FORMAT = "Fetching %d new items from server. %d fetched out of total %d";
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
    private final ConfigService configService;
    private final CheckServerUpdatesWorkerFactory factory;


    @Inject
    public CheckServerUpdatesWorker(ItemsService itemsService, ApplicationModel applicationModel,
                                    AccountMapper accountMapper, ProductMapper productMapper, ProductsService productsService,
                                    ArtistsService artistsService, ArtistMapper artistMapper,
                                    ConfigurationMapper configurationMapper, FileDownloader fileDownloader,
                                    ItemManager itemManager, AccountManager accountManager, ConfigService configService,
                                    CheckServerUpdatesWorkerFactory factory) {
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
        this.configService = configService;
        this.factory = factory;
    }


    @Override
    protected Void doInBackground() throws Exception {
        publish(new CheckServerStatusStage(null));
        Account account = accountManager.getCurrentAccount();
        Long loadedLastOrderId = account.getLastOrderId();
        ItemsMetadataDto ordersMetadata = itemsService.getOrdersMetadata(loadedLastOrderId);

        Config config = configService.getConfig();

        int fetchedItems = 0;
        if (0 != ordersMetadata.getTotalItems()) {
            LogEvent metadataFetchedLog = new LogEvent(String.format(METADATA_FETCHED_FORMAT, ordersMetadata.getTotalItems()));
            publish(new CheckServerStatusStage(metadataFetchedLog));
            int pageSize = config.getMaxPageSize();
            for (int i = 1; (i - 1) * pageSize < ordersMetadata.getTotalItems(); i++) {
                int itemsToFetch = ordersMetadata.getTotalItems() - (i - 1) * pageSize;
                if (itemsToFetch > pageSize) {
                    itemsToFetch = pageSize;
                }
                LogEvent itemsFetchedLog = new LogEvent(String.format(ITEMS_FETCHED_FORMAT, itemsToFetch,
                        fetchedItems, ordersMetadata.getTotalItems()));

                ItemsDto tracks = itemsService.getTracks(loadedLastOrderId, ordersMetadata.getLastItemId(), i, pageSize);
                fetchedItems += pageSize;
                if (fetchedItems > ordersMetadata.getTotalItems()) {
                    fetchedItems = ordersMetadata.getTotalItems();
                }
                publish(new CheckServerStatusStage(itemsFetchedLog));

                for (Item item : tracks.getItems()) {
                    itemManager.saveItem(item);
                }
            }
            account.setLastOrderId(ordersMetadata.getLastItemId());
            accountMapper.updateAccount(account);
        }


        fetch(config, new ProductFetcherCallback(), "products");
        fetch(config, new ArtistFetcherCallback(), "artists");

        if (configurationMapper.getAutoDownload()) {
            for (Item item : itemManager.findItemByStatus(Item.AVAILABLE)) {
                fileDownloader.scheduleDownload(item);
            }
        }
        return null;
    }

    private <E> void fetch(Config config, FetcherCallback<E> callback, String name) throws IOException, RequestException {
        int totalItems = callback.itemsToFetch().size();
        int fetchedItems = 0;

        while (!callback.itemsToFetch().isEmpty()) {
            List<Long> unknownItems = callback.itemsToFetch();
            if (unknownItems.size() > config.getMaxPageSize()) {
                unknownItems = unknownItems.subList(0, config.getMaxPageSize());
            }

            publish(new CheckServerStatusStage(new LogEvent(String.format("Fetching new %2$d %1$s from server. %3$d fetched out of total %4$d.",
                    name, unknownItems.size(), fetchedItems, totalItems))));

            List<E> fetch = callback.fetch(unknownItems);
            fetchedItems += unknownItems.size();
            for (E e : fetch) {
                callback.merge(e);
                unknownItems.remove(callback.getId(e));
            }

            for (long id : unknownItems) {
                // We requested server for those IPs but it didn't return
                // Put stub here
                callback.putStub(id);
            }
        }
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

        factory.done();
    }

    private interface FetcherCallback<E> {
        List<Long> itemsToFetch();

        List<E> fetch(List<Long> unknownItems) throws IOException, RequestException;

        void merge(E e);

        Long getId(E e);

        void putStub(long id);
    }

    private class ProductFetcherCallback implements FetcherCallback<Product> {
        @Override
        public List<Long> itemsToFetch() {
            return productMapper.findUnknownProducts();
        }

        @Override
        public List<Product> fetch(List<Long> unknownItems) throws IOException, RequestException {
            return productsService.getProducts(unknownItems);
        }

        @Override
        public void merge(Product product) {
            if (productMapper.productExists(product.getProductId())) {
                productMapper.updateProduct(product);
            } else {
                productMapper.insertProduct(product);
            }
        }

        @Override
        public Long getId(Product product) {
            return product.getProductId();
        }

        @Override
        public void putStub(long id) {
            if (!productMapper.productExists(id)) {
                Product unknownProductStub = new Product();
                unknownProductStub.setProductId(id);
                unknownProductStub.setName("UNKNOWN");
                unknownProductStub.setDescription("UNKNOWN");
                productMapper.insertProduct(unknownProductStub);
            }
        }
    }

    private class ArtistFetcherCallback implements FetcherCallback<Artist> {
        @Override
        public List<Long> itemsToFetch() {
            return artistMapper.findUnknownArtists();
        }

        @Override
        public List<Artist> fetch(List<Long> unknownItems) throws IOException, RequestException {
            return artistsService.getArtists(unknownItems);
        }

        @Override
        public void merge(Artist artist) {
            if (artistMapper.artistExists(artist.getArtistId())) {
                artistMapper.updateArtist(artist);
            } else {
                artistMapper.insertArtist(artist);
            }
        }

        @Override
        public Long getId(Artist artist) {
            return artist.getArtistId();
        }

        @Override
        public void putStub(long id) {
            if (!artistMapper.artistExists(id)) {
                Artist unknownArtistStub = new Artist();
                unknownArtistStub.setArtistId(id);
                artistMapper.insertArtist(unknownArtistStub);
            }
        }
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
