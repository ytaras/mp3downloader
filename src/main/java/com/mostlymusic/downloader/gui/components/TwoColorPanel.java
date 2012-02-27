package com.mostlymusic.downloader.gui.components;

import javax.swing.*;
import java.awt.*;

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
        g.setColor(upperColor);
        g.fillRect(0, 0, getWidth(), Math.min(firstBandHeight, getHeight()));
    }

    public int getFirstBandHeight() {
        return firstBandHeight;
    }

    public void setFirstBandHeight(int firstBandHeight) {
        this.firstBandHeight = firstBandHeight;
    }

    public Color getUpperColor() {
        return upperColor;
    }

    public void setUpperColor(Color upperColor) {
        this.upperColor = upperColor;
    }
}
