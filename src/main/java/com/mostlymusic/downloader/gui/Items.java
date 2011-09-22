package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.gui.worker.DownloadFileWorker;

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
    private ItemsTableModel itemsTableModel;

    @Inject
    public Items(final DownloadFileWorker downloadFileWorker) {
        itemsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                downloadFileButton.setEnabled(itemsTable.getSelectedRow() >= 0);
            }
        });
        downloadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                downloadFileWorker.setItemToDownload(itemsTableModel.getItemAt(itemsTable.getSelectedRow()));
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
