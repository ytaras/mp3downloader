package com.mostlymusic.downloader.gui.laf;

import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthPainter;
import java.awt.*;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public class ButtonBackgroundPainter extends SynthPainter {

    @Override
    public void paintButtonBackground(SynthContext context, Graphics g, int x, int y, int w, int h) {
        Graphics2D g2 = (Graphics2D) g;
        GradientPaint paint = new GradientPaint(x, y, Color.decode("#99cc00"), x, (y + h), new Color(51, 102, 0));
        g2.setPaint(paint);
        g2.fillRect(x, y, w, h);
        g2.setPaint(null);
    }
}
