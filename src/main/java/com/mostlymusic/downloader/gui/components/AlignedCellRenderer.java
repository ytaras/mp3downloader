package com.mostlymusic.downloader.gui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public class AlignedCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel tableCellRendererComponent = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        tableCellRendererComponent.setVerticalTextPosition(SwingConstants.CENTER);
        // FIXME A dirty hack to introduce padding
        tableCellRendererComponent.setText("<html><div style=\"padding:10px\">" + tableCellRendererComponent.getText() + "</div>");
        return tableCellRendererComponent;
    }

}
