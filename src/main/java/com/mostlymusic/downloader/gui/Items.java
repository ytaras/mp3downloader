package com.mostlymusic.downloader.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.client.Product;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.components.ItemStatusRenderer;
import com.mostlymusic.downloader.gui.components.JImagePane;
import com.mostlymusic.downloader.gui.worker.FileDownloader;

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
    private JPanel leftPanel;
    private JScrollPane descriptionScrollPane;
    private ItemsTableModel itemsTableModel;

    @Inject
    public Items(final FileDownloader fileDownloader) {
        // itemsTable.setDefaultRenderer(Object.class, new MultiLineTableCellRenderer());
        Dimension imageDim = new Dimension(-1, 240);
        image.setMaximumSize(imageDim);
        image.setPreferredSize(imageDim);
        itemsTable.setDefaultRenderer(ItemsTableModel.ItemStatus.class, new ItemStatusRenderer());
        itemsTable.setRowHeight(30);
        // FIXME Hardcode
        itemsTable.setForeground(Color.decode("#79ac00"));
        itemsTable.setBackground(Color.decode("#142019"));
        itemsTable.setSelectionBackground(Color.decode("#273f32"));
        itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (itemsTable.getSelectedRow() >= 0) {
                    final Product product = itemsTableModel.getProductAt(itemsTable.getSelectedRow());
                    if (product != null) {
                        description.setText("<html>" + product.getDescription() + "</html>");
                    } else {
                        description.setText("");
                    }
                    descriptionScrollPane.setVisible(true);
                    image.setImage(null);
                    if (product != null) {
                        new ImageFetcherSwingWorker(product, itemsTable.getSelectedRow()).execute();
                    }
                } else {
                    image.setVisible(false);
                    descriptionScrollPane.setVisible(false);
                }
                for (int row : itemsTable.getSelectedRows()) {
                    if (!itemsTableModel.isDownloadingItemAt(row) && !itemsTableModel.isScheduledItemAt(row)) {

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
        image.setPreferredWidth(240);

        MatteBorder border = BorderFactory.createMatteBorder(0, 2, 2, 2, Color.decode("#79ac00"));
        contentPane.setBorder(border);
        leftPanel.setUI(new BasicPanelUI());
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    @Inject
    public void setApplicationModel(ApplicationModel applicationModel) {
        itemsTableModel = applicationModel.getItemsTableModel();
        itemsTable.setModel(itemsTableModel);
        setWidthAsPercentages(itemsTable, 0.2, 0.2, 0.4, 0.2);
        itemsTable.getColumnModel().getColumn(0).setMinWidth(80);
        itemsTable.getColumnModel().getColumn(1).setMinWidth(80);
        itemsTable.getColumnModel().getColumn(2).setMinWidth(120);
        itemsTable.getColumnModel().getColumn(3).setMaxWidth(140);
        itemsTable.getColumnModel().getColumn(3).setMinWidth(110);
    }

    private static void setWidthAsPercentages(JTable table,
                                              double... percentages) {
        final double factor = 10000;

        TableColumnModel model = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < percentages.length; columnIndex++) {
            TableColumn column = model.getColumn(columnIndex);
            column.setPreferredWidth((int) (percentages[columnIndex] * factor));
        }
    }


    private void createUIComponents() {
        leftPanel = new JPanel();
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
            if (product.getMainImage() != null && product.getMainImage() != null && !product.getMainImage().isEmpty()) {
                return ImageIO.read(new URL(product.getMainImage()));
            }
            return null;
        }

        @Override
        protected void done() {
            try {
                if (isSelectionValid(itemsTableModel)) {
                    image.setImage(get());
                    image.setVisible(true);
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


