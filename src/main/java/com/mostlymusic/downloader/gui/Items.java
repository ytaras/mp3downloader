package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.client.Product;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.components.ItemStatusRenderer;
import com.mostlymusic.downloader.gui.worker.DownloadFileWorker;
import com.mostlymusic.downloader.gui.worker.DownloadFileWorkerFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
    private JTextArea description;
    private JLabel image;
    private ItemsTableModel itemsTableModel;

    @Inject
    public Items(final DownloadFileWorkerFactory downloadFileWorkerFactory) {
        itemsTable.setDefaultRenderer(ItemsTableModel.ItemStatus.class, new ItemStatusRenderer());
        itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (itemsTable.getSelectedRow() >= 0) {
                    Product product = itemsTableModel.getProductAt(itemsTable.getSelectedRow());
                    description.setText(product.getDescription());
                    image.setText("<html><img src='" + product.getMainImage() + "'");
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
