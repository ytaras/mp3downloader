package com.mostlymusic.downloader.gui;

import java.awt.*;
import javax.swing.*;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public interface ConfigurationManager {
    void save(Frame mainWindow);

    void load(JFrame mainWindow);
}
