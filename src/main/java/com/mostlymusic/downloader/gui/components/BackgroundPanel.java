package com.mostlymusic.downloader.gui.components;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
 *  Support custom painting on a panel in the form of
 *
 *  a) images - that can be scaled, tiled or painted at original size
 *  b) non solid painting - that can be done by using a Paint object
 *
 *  Also, any component added directly to this panel will be made
 *  non-opaque so that the custom painting can show through.
 */
public class BackgroundPanel extends JPanel {

    public BackgroundPanel(String image, Style style) throws IOException {
        this(ImageIO.read(BackgroundPanel.class.getResourceAsStream(image)), style, 0, 0);
    }

    public BackgroundPanel(Image image, Style style) {
        this(image, style, 0, 0);
    }

    public Image getImage() {
        return image;
    }

    public enum Style {
        SCALED, TILED, ACTUAL
    }

    private Image image;
    private Style style = Style.SCALED;
    private float alignmentX = 0.5f;
    private float alignmentY = 0.5f;

    /*
      *  Set image as the backround with the specified style and alignment
      */
    public BackgroundPanel(Image image, Style style, float alignmentX, float alignmentY) {
        setImage(image);
        setStyle(style);
        setImageAlignmentX(alignmentX);
        setImageAlignmentY(alignmentY);
        setLayout(new BorderLayout());
    }

    /*
      *	Set the image used as the background
      */
    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    /*
      *	Set the style used to paint the background image
      */
    public void setStyle(Style style) {
        this.style = style;
        repaint();
    }

    /*
      *  Specify the horizontal alignment of the image when using ACTUAL style
      */
    public void setImageAlignmentX(float alignmentX) {
        this.alignmentX = alignmentX > 1.0f ? 1.0f : alignmentX < 0.0f ? 0.0f : alignmentX;
        repaint();
    }

    /*
      *  Specify the horizontal alignment of the image when using ACTUAL style
      */
    public void setImageAlignmentY(float alignmentY) {
        this.alignmentY = alignmentY > 1.0f ? 1.0f : alignmentY < 0.0f ? 0.0f : alignmentY;
        repaint();
    }

    /*
      *  Override method so we can make the component transparent
      */
    @SuppressWarnings("NullableProblems")
    public void add(JComponent component) {
        add(component, null);
    }

    /*
      *  Override to provide a preferred size equal to the image size
      */
    @Override
    public Dimension getPreferredSize() {
        if (image == null)
            return super.getPreferredSize();
        else
            return new Dimension(image.getWidth(null), image.getHeight(null));
    }

    /*
      *  Override method so we can make the component transparent
      */
    public void add(JComponent component, Object constraints) {
        makeComponentTransparent(component);

        super.add(component, constraints);
    }

    /*
      *	Try to make the component transparent.
      *  For components that use renderers, like JTable, you will also need to
      *  change the renderer to be transparent. An easy way to do this it to
      *  set the background of the table to a Color using an alpha value of 0.
      */
    private void makeComponentTransparent(JComponent component) {
        component.setOpaque(false);

        if (component instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) component;
            JViewport viewport = scrollPane.getViewport();
            viewport.setOpaque(false);
            Component c = viewport.getView();

            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(false);
            }
        }
    }

    /*
      *  Add custom painting
      */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //  Invoke the painter for the background
        beforeDrawImage(g);
        //  Draw the image
        if (image == null) return;

        switch (style) {
            case SCALED:
                drawScaled(g);
                break;

            case TILED:
                drawTiled(g);
                break;

            case ACTUAL:
                drawActual(g);
                break;

            default:
                drawScaled(g);
        }
    }

    protected void beforeDrawImage(Graphics g) {
    }

    /*
      *  Custom painting code for drawing a SCALED image as the background
      */
    private void drawScaled(Graphics g) {
        Dimension d = getSize();
        g.drawImage(image, 0, 0, d.width, d.height, null);
    }

    /*
      *  Custom painting code for drawing TILED images as the background
      */
    private void drawTiled(Graphics g) {
        Dimension d = getSize();
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        for (int x = 0; x < d.width; x += width) {
            for (int y = 0; y < d.height; y += height) {
                g.drawImage(image, x, y, null, null);
            }
        }
    }

    /*
      *  Custom painting code for drawing the ACTUAL image as the background.
      *  The image is positioned in the panel based on the horizontal and
      *  vertical alignments specified.
      */
    private void drawActual(Graphics g) {
        Dimension d = getSize();
        Insets insets = getInsets();
        int width = d.width - insets.left - insets.right;
        int height = d.height - insets.top - insets.left;
        float x = (width - image.getWidth(null)) * alignmentX;
        float y = (height - image.getHeight(null)) * alignmentY;
        g.drawImage(image, (int) x + insets.left, (int) y + insets.top, this);
    }

}
