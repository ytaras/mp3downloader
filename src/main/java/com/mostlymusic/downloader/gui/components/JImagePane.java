package com.mostlymusic.downloader.gui.components;

import javax.swing.*;
import java.awt.*;

/**
 * @author ytaras
 *         Date: 9/27/11
 *         Time: 12:16 PM
 */
public class JImagePane extends JPanel {
    private Image image;
    private JLabel textLabel = new JLabel();

    public JImagePane() {
        add(textLabel);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (null != image) {
            Dimension size = getSize();
            size = getImageSize(size);
            graphics.drawImage(image, 0, 0, size.width, size.height, null);
        } else {
            super.paintComponent(graphics);
        }

    }

    private Dimension getImageSize(Dimension size) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (size.width >= width && size.height >= height) {
            return new Dimension(width, height);
        } else {
            double widthScale = ((double) width) / size.width;
            double heightScale = ((double) height) / size.height;
            double scale = Math.max(widthScale, heightScale);
            int newWidth = (int) (width / scale);
            int newHeight = (int) (width / scale);
            return new Dimension(newWidth, newHeight);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (null == image) {
            return super.getPreferredSize();
        } else {
            return new Dimension(image.getWidth(null), image.getHeight(null));
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setNoImageText(String text) {
        textLabel.setText(text);
    }

    public String getNoImageText() {
        return textLabel.getText();
    }
}
