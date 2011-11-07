package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.client.Product;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.components.ItemStatusRenderer;
import com.mostlymusic.downloader.gui.components.JImagePane;
import com.mostlymusic.downloader.gui.worker.DownloadFileWorker;
import com.mostlymusic.downloader.gui.worker.DownloadFileWorkerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;

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
    private JPanel itemsPane;
    private JLabel description;
    private JImagePane image;
    private JSplitPane splitPane;
    private ItemsTableModel itemsTableModel;

    @Inject
    public Items(final DownloadFileWorkerFactory downloadFileWorkerFactory) {
        itemsTable.setDefaultRenderer(ItemsTableModel.ItemStatus.class, new ItemStatusRenderer());
        itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (itemsTable.getSelectedRow() >= 0) {
                    final Product product = itemsTableModel.getProductAt(itemsTable.getSelectedRow());
                    description.setText("<html>" + product.getDescription() + "</html>");
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                image.setImage(null);
                                BufferedImage bufferedImage = ImageIO.read(new URL(product.getMainImage()));
                                image.setImage(bufferedImage);
                            } catch (IOException e) {
                                image.setImage(null);
                            }

                        }
                    });
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
                    DownloadFileWorker downloadFileWorker =
                            downloadFileWorkerFactory.createWorker(item);
                    downloadFileWorker.addPropertyChangeListener(new PropertyChangeListener() {
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
                    downloadFileWorker.execute();
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
}
