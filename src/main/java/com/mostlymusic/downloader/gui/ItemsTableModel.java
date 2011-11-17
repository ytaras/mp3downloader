package com.mostlymusic.downloader.gui;

import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.client.Product;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.localdata.ArtistMapper;
import com.mostlymusic.downloader.localdata.ItemMapper;
import com.mostlymusic.downloader.localdata.ProductMapper;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * @author ytaras
 *         Date: 9/21/11
 *         Time: 10:41 AM
 */
public class ItemsTableModel extends AbstractTableModel {
    private ApplicationModel applicationModel;
    private ItemMapper itemMapper;
    private ProductMapper productMapper;
    private final ArtistMapper artistMapper;
    private List<Item> data;
    private Map<Long, Long> downloadProgress = new HashMap<Long, Long>();
    private Map<Long, Product> products = new HashMap<Long, Product>();
    private Map<Long, Artist> artists = new HashMap<Long, Artist>();
    private Map<Long, Long> fileSizes = new HashMap<Long, Long>();
    private static final String ITEM_ID = "Item id";
    private static final String TITLE = "Title";
    private static final String STATUS = "Status";
    private static final String DOWNLOADS_BOUGHT = "Downloads bought";
    private static final String DOWNLOADS_USED = "Downloads used";
    private static final String ISSUED_AT = "Issued at";
    private static final String ARTIST_NAME = "Artist name";
    private static final String PRODUCT_NAME = "Product name";
    private static final String[] COLUMN_NAMES = new String[]{ARTIST_NAME, PRODUCT_NAME, TITLE, STATUS};
    private Map<Long, Integer> itemIdToRowMap = Collections.emptyMap();


    public ItemsTableModel(ApplicationModel applicationModel, ItemMapper itemMapper,
                           ProductMapper productMapper, ArtistMapper artistMapper) {
        this.applicationModel = applicationModel;
        this.itemMapper = itemMapper;
        this.productMapper = productMapper;
        this.artistMapper = artistMapper;
        addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                refresh();
            }
        });
    }

    private void refresh() {
        Account loggedAccount = applicationModel.getLoggedAccount();
        if (null == loggedAccount) {
            this.data = Collections.emptyList();
            this.itemIdToRowMap = Collections.emptyMap();
        } else {
            this.data = itemMapper.listLinks(loggedAccount);
            this.itemIdToRowMap = new HashMap<Long, Integer>();
            List<Long> productIds = new LinkedList<Long>();
            for (int row = 0; row < data.size(); row++) {
                Item item = data.get(row);
                itemIdToRowMap.put(item.getItemId(), row);
                productIds.add(item.getProductId());
            }
            for (Long productId : productIds) {
                products.put(productId, productMapper.loadProduct(productId));
            }
            for (Artist artist : artistMapper.listArtists()) {
                artists.put(artist.getArtistId(), artist);
            }
        }
    }

    @Override
    public int getRowCount() {
        return getData().size();
    }

    private Product getProduct(long id) {
        return products.get(id);
    }

    private Artist getArtist(long id) {
        return artists.get(id);
    }


    private synchronized List<Item> getData() {
        if (null == data) {
            refresh();
        }
        return data;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Item item = getItemAt(row);
        // No time to put full-blown binding framework here :(
        // So I have to use this ugly solution to protect myself from mistypes

        String columnName = COLUMN_NAMES[col];
        if (ITEM_ID.equals(columnName)) {
            return item.getItemId();
        } else if (TITLE.equals(columnName)) {
            return item.getLinkTitle();
        } else if (STATUS.equals(columnName)) {
            return new ItemStatus(row);
        } else if (DOWNLOADS_BOUGHT.equals(columnName)) {
            return item.getDownloadsBought();
        } else if (DOWNLOADS_USED.equals(columnName)) {
            return item.getDownloadsUsed();
        } else if (ISSUED_AT.equals(columnName)) {
            return item.getCreatedAt();
        } else if (ARTIST_NAME.equals(columnName)) {
            return getArtist(item.getMainArtistId()).getName();
        } else if (PRODUCT_NAME.equals(columnName)) {
            return getProduct(item.getProductId()).getName();
        }
        throw new RuntimeException("Unknown column - " + columnName);

    }

    @Override
    public String getColumnName(int i) {
        return COLUMN_NAMES[i];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        if (STATUS.equals(COLUMN_NAMES[i])) {
            return ItemStatus.class;
        }
        return super.getColumnClass(i);
    }

    public Item getItemAt(int row) {
        return getData().get(row);
    }

    public boolean isDownloadingItemAt(int row) {
        return downloadProgress.containsKey(getItemAt(row).getItemId());
    }

    public void downloadStopped(Item item) {
        downloadProgress.remove(item.getItemId());
        fireTableCellUpdated(getItemRow(item), getColumn(STATUS));
    }

    public void downloadStarted(Item item) {
        downloadProgress.put(item.getItemId(), 0L);
        fireTableCellUpdated(getItemRow(item), getColumn(STATUS));
    }

    private long getDownloadProgress(Item item) {
        return downloadProgress.get(item.getItemId());
    }

    public void setDownloadProgress(Item item, Long progress) {
        Long oldValue = downloadProgress.get(item.getItemId());
        if (!progress.equals(oldValue)) {
            downloadProgress.put(item.getItemId(), progress);
            fireTableCellUpdated(getItemRow(item), getColumn(STATUS));
        }
    }

    private int getColumn(String status) {
        for (int col = 0; col < COLUMN_NAMES.length; col++) {
            if (COLUMN_NAMES[col].equals(status)) {
                return col;
            }
        }
        return -1;
    }

    private int getItemRow(Item item) {
        return itemIdToRowMap.get(item.getItemId());
    }

    public void setFileSize(Item item, Long value) {
        fileSizes.put(item.getItemId(), value);
    }

    public Product getProductAt(int selectedRow) {
        return getProduct(getItemAt(selectedRow).getProductId());
    }


    public class ItemStatus {
        private final int row;

        public ItemStatus(int row) {
            this.row = row;
        }

        public String getStatus() {
            return getItemAt(row).getStatus();
        }

        @Override
        public String toString() {
            return getStatus();
        }

        public boolean isDownloading() {
            return isDownloadingItemAt(row);
        }


        public long getDownloadProgress() {
            return ItemsTableModel.this.getDownloadProgress(getItemAt(row));
        }

        public Long getTotalSize() {
            return fileSizes.get(getItemAt(row).getItemId());
        }
    }

}
