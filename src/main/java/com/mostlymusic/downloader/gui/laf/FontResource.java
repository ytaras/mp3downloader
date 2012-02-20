package com.mostlymusic.downloader.gui.laf;

import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public class FontResource {
    private static final String FONT_PATH = "/fonts/font-%s.ttf";

    public static FontUIResource createFont(String name, int size) throws IOException, FontFormatException {
        InputStream resource = FontResource.class.getResourceAsStream(String.format(FONT_PATH, name));
        Font font = Font.createFont(Font.TRUETYPE_FONT, resource);
        return new FontUIResource(font.deriveFont((float) size));
    }
}
