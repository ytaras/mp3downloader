package com.mostlymusic.downloader.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProgressGlassPane extends JComponent {
    private static final int BAR_WIDTH = 200;
    private static final int BAR_HEIGHT = 10;

    private static final Color TEXT_COLOR = new Color(0x333333);

    private String message = "";

    public ProgressGlassPane() {
        setBackground(Color.WHITE);
        setFont(new Font("Default", Font.BOLD, 16));
        interceptEvents();
    }

    private void interceptEvents() {
        addMouseListener(new MouseAdapter() {
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
        addKeyListener(new KeyAdapter() {
        });

        setFocusTraversalKeysEnabled(false);
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent evt) {
                requestFocusInWindow();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // enables anti-aliasing
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // gets the current clipping area
        Rectangle clip = g.getClipBounds();

        // sets a 65% translucent composite
        AlphaComposite alpha = AlphaComposite.SrcOver.derive(0.65f);
        Composite composite = g2.getComposite();
        g2.setComposite(alpha);

        // fills the background
        g2.setColor(getBackground());
        g2.fillRect(clip.x, clip.y, clip.width, clip.height);

        // centers the progress bar on screen
        FontMetrics metrics = g.getFontMetrics();
        int x = (getWidth() - BAR_WIDTH) / 2;
        int y = (getHeight() - BAR_HEIGHT - metrics.getDescent()) / 2;

        // draws the text
        g2.setColor(TEXT_COLOR);
        g2.drawString(message, x, y);
    }

    public void setMessage(String message) {
        this.message = message;
    }
}