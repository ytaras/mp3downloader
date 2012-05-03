package com.mostlymusic.downloader.gui.components;

import java.awt.*;
import javax.swing.*;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public class TwoColorPanel extends JPanel {
    private Color upperColor;
    private int firstBandHeight;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(upperColor);
        g.fillRect(0, 0, getWidth(), Math.min(firstBandHeight, getHeight()));
    }

    public void setFirstBandHeight(int firstBandHeight) {
        this.firstBandHeight = firstBandHeight;
    }

    public void setUpperColor(Color upperColor) {
        this.upperColor = upperColor;
    }
}
