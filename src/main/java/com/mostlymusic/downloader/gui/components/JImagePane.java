package com.mostlymusic.downloader.gui.components;

import java.awt.*;
import javax.swing.*;

import org.jetbrains.annotations.Nullable;

/**
 * @author ytaras
 *         Date: 9/27/11
 *         Time: 12:16 PM
 */
public class JImagePane extends JPanel {
    private Image image;
    private final JLabel textLabel = new JLabel();
    private int preferredWidth;
    private int preferredHeight;
    private Dimension preferredSize;

    public JImagePane() {
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (null != image) {
            graphics.drawImage(image, 0, 0, preferredSize.width, preferredSize.height, null);
        } else {
            super.paintComponent(graphics);
        }
    }

    private void recalculateImageSize() {
        if (image == null) {
            preferredSize = new Dimension(-1, -1);
        } else {
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            double ratio = 1.0 * height / width;
            preferredHeight = (int) Math.round(ratio * this.preferredWidth);
            preferredSize = new Dimension(preferredWidth, preferredHeight);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return preferredSize;
    }

    @Override
    public Dimension getMaximumSize() {
        return preferredSize;
    }

    @Override
    public Dimension getMinimumSize() {
        return preferredSize;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(@Nullable Image image) {
        this.image = image;
        recalculateImageSize();
        revalidate();
        repaint();
    }

    public void setNoImageText(String text) {
        textLabel.setText(text);
    }

    public String getNoImageText() {
        return textLabel.getText();
    }

    public void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;
    }
}
