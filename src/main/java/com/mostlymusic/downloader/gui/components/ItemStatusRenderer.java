package com.mostlymusic.downloader.gui.components;

import com.mostlymusic.downloader.gui.ItemsTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 3:09 PM
 */
public class ItemStatusRenderer extends DefaultTableCellRenderer {
    private JProgressBar progressBar = new JProgressBar();

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
        ItemsTableModel.ItemStatus status = (ItemsTableModel.ItemStatus) object;
        if (status.isDownloading()) {
            Long totalSize = status.getTotalSize();
            if (null == totalSize || totalSize < 0) {
                progressBar.setValue(0);
            } else if (totalSize >= Integer.MAX_VALUE) {
                progressBar.setMaximum((int) (totalSize >> 32));
                progressBar.setValue((int) (status.getDownloadProgress() >> 32));
                progressBar.setMinimum(0);
            } else {
                progressBar.setMaximum(totalSize.intValue());
                progressBar.setValue((int) (status.getDownloadProgress()));
                progressBar.setMinimum(0);
            }
            return progressBar;
        }
        return super.getTableCellRendererComponent(jTable, object, isSelected, hasFocus, row, column);
    }
}
