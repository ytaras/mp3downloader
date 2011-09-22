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

    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object object, boolean isSelected, boolean hasFocus, int row, int column) {
        ItemsTableModel.ItemStatus status = (ItemsTableModel.ItemStatus) object;
        if (status.isDownloading()) {
            JLabel tableCellRendererComponent = (JLabel) super.getTableCellRendererComponent(jTable, object, isSelected, hasFocus, row, column);
            tableCellRendererComponent.setText("DOWNLOADING...");
            return tableCellRendererComponent;
        }
        return super.getTableCellRendererComponent(jTable, object, isSelected, hasFocus, row, column);
    }
}
