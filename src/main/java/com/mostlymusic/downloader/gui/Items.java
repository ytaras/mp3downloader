package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.client.Product;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.components.AlignedCellRenderer;
import com.mostlymusic.downloader.gui.components.ItemStatusRenderer;
import com.mostlymusic.downloader.gui.components.JImagePane;
import com.mostlymusic.downloader.gui.worker.FileDownloader;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 3:41 PM
 */
@Singleton
public class Items {
    private JPanel contentPane;
    private JTable itemsTable;
    private JButton downloadFileButton;
    private JLabel description;
    private JImagePane image;
    private JSplitPane splitPane;
    private ItemsTableModel itemsTableModel;

    @Inject
    public Items(final FileDownloader fileDownloader) {
        itemsTable.setDefaultRenderer(Object.class, new AlignedCellRenderer());
        itemsTable.setDefaultRenderer(ItemsTableModel.ItemStatus.class, new ItemStatusRenderer());
        itemsTable.setRowHeight(30);
        // FIXME Hardcode
        itemsTable.setForeground(Color.decode("#79ac00"));
        itemsTable.setBackground(Color.decode("#142019"));
        itemsTable.setSelectionForeground(itemsTable.getBackground());
        itemsTable.setSelectionBackground(itemsTable.getForeground());
        itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (itemsTable.getSelectedRow() >= 0) {
                    final Product product = itemsTableModel.getProductAt(itemsTable.getSelectedRow());
                    description.setText("<html>" + product.getDescription() + "</html>");
                    image.setImage(null);
                    new ImageFetcherSwingWorker(product, itemsTable.getSelectedRow()).execute();
                }
                for (int row : itemsTable.getSelectedRows()) {
                    if (!itemsTableModel.isDownloadingItemAt(row)) {
                        downloadFileButton.setEnabled(true);
                        return;
                    }
                }
                downloadFileButton.setEnabled(false);
            }
        });
        downloadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (int row : itemsTable.getSelectedRows()) {
                    if (itemsTableModel.isDownloadingItemAt(row)) {
                        continue;
                    }
                    Item item = itemsTableModel.getItemAt(row);
                    fileDownloader.scheduleDownload(item, new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                            if ("state".equals(propertyChangeEvent.getPropertyName())) {
                                final SwingWorker.StateValue newValue = (SwingWorker.StateValue) propertyChangeEvent.getNewValue();
                                if (newValue == SwingWorker.StateValue.PENDING || newValue == SwingWorker.StateValue.STARTED) {
                                    downloadFileButton.setEnabled(false);
                                } else if (SwingWorker.StateValue.DONE == newValue) {
                                    downloadFileButton.setEnabled(true);
                                }
                            }
                        }
                    });
                }
            }
        });
        splitPane.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                image.setPreferredWidth(splitPane.getDividerLocation());
            }
        });
        image.setPreferredWidth(splitPane.getDividerLocation());
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    @Inject
    public void setApplicationModel(ApplicationModel applicationModel) {
        itemsTableModel = applicationModel.getItemsTableModel();
        itemsTable.setModel(itemsTableModel);
    }

    private class ImageFetcherSwingWorker extends SwingWorker<Image, Void> {
        private final Product product;
        private final int itemsTableModel;

        public ImageFetcherSwingWorker(Product product, int selectedRow) {
            this.product = product;
            this.itemsTableModel = selectedRow;
        }

        @Override
        protected Image doInBackground() throws Exception {
            return ImageIO.read(new URL(product.getMainImage()));
        }

        @Override
        protected void done() {
            try {
                if (isSelectionValid(itemsTableModel)) {
                    image.setImage(get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSelectionValid(int row) {
        return itemsTable.getSelectedRow() == row;
    }
}
