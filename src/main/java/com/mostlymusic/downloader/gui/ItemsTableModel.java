package com.mostlymusic.downloader.gui;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.client.Artist;
import com.mostlymusic.downloader.client.Product;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.manager.ArtistMapper;
import com.mostlymusic.downloader.manager.ItemManager;
import com.mostlymusic.downloader.manager.ItemMapperListener;
import com.mostlymusic.downloader.manager.ProductMapper;

/**
 * @author ytaras
 *         Date: 9/21/11
 *         Time: 10:41 AM
 */
@Singleton
public class ItemsTableModel extends AbstractTableModel {
    private final ProductMapper productMapper;
    private final ArtistMapper artistMapper;
    private List<Item> data;
    private final Map<Long, Long> downloadProgress = new HashMap<Long, Long>();
    private final Map<Long, Product> products = new HashMap<Long, Product>();
    private final Map<Long, Artist> artists = new HashMap<Long, Artist>();
    private final Map<Long, Long> fileSizes = new HashMap<Long, Long>();
    private static final String ITEM_ID = "Item id";
    private static final String TITLE = "Title";
    private static final String STATUS = "Status";
    private static final String DOWNLOADS_BOUGHT = "Downloads bought";
    private static final String DOWNLOADS_USED = "Downloads used";
    private static final String ISSUED_AT = "Issued at";
    private static final String ARTIST_NAME = "Artist name";
    private static final String PRODUCT_NAME = "Product name";
    private static final String[] COLUMN_NAMES = new String[]{ARTIST_NAME, PRODUCT_NAME, TITLE, STATUS};
    private final Map<Long, Integer> itemIdToRowMap = Collections.synchronizedMap(new HashMap<Long, Integer>());
    private final ItemManager itemManager;

    @Inject
    public ItemsTableModel(ItemManager itemManager, ProductMapper productMapper, ArtistMapper artistMapper) {
        this.productMapper = productMapper;
        this.artistMapper = artistMapper;
        addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent tableModelEvent) {
                refresh();
            }
        });
        this.itemManager = itemManager;
        itemManager.addListener(new ItemMapperListener() {
            @Override
            public void addedItem(Item item, Account account) {
                refresh();
            }

            @Override
            public void updatedItem(Item item) {
                refresh();
            }
        });
    }

    private void refresh() {
        this.data = itemManager.findItem();
        List<Long> productIds = new LinkedList<Long>();
        Map<Long, Integer> newMap = new HashMap<Long, Integer>();
        for (int row = 0; row < data.size(); row++) {
            Item item = data.get(row);
            newMap.put(item.getItemId(), row);
            productIds.add(item.getProductId());
        }

        synchronized (itemIdToRowMap) {
            itemIdToRowMap.clear();
            itemIdToRowMap.putAll(newMap);
        }

        for (Long productId : productIds) {
            products.put(productId, productMapper.loadProduct(productId));
        }
        for (Artist artist : artistMapper.listArtists()) {
            artists.put(artist.getArtistId(), artist);
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
            Artist artist = getArtist(item.getMainArtistId());
            if (null == artist) {
                return "";
            }
            return artist.getName();
        } else if (PRODUCT_NAME.equals(columnName)) {
            Product product = getProduct(item.getProductId());
            if (null == product) {
                return "";
            }
            return product.getName();
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
        fireStatusCellUpdated(item);
    }

    public void downloadStarted(Item item) {
        downloadProgress.put(item.getItemId(), 0L);
        fireStatusCellUpdated(item);
    }

    private void fireStatusCellUpdated(Item item) {
        Integer itemRow = getItemRow(item);
        if (itemRow != null) {
            fireTableCellUpdated(itemRow, getColumn(STATUS));
        }
    }

    private long getDownloadProgress(Item item) {
        return downloadProgress.get(item.getItemId());
    }

    public void setDownloadProgress(Item item, Long progress) {
        Long oldValue = downloadProgress.get(item.getItemId());
        if (!progress.equals(oldValue)) {
            downloadProgress.put(item.getItemId(), progress);
            fireStatusCellUpdated(item);
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

    private Integer getItemRow(Item item) {
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
