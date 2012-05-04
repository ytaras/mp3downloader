package com.mostlymusic.downloader.gui;

import java.awt.*;

import javax.swing.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.manager.ConfigurationMapper;
import com.mostlymusic.downloader.manager.FrameSize;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
@Singleton
public class MapperConfigurationManager implements ConfigurationManager{
    private final ConfigurationMapper mapper;

    @Inject
    public MapperConfigurationManager(ConfigurationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void save(Frame mainWindow) {
        mapper.setFrameSize(new FrameSize(mainWindow.getSize(), mainWindow.getLocation()));
    }

    @Override
    public void load(JFrame mainWindow) {
        FrameSize frameSize = mapper.getFrameSize();
        if(frameSize == null) {
            return;
        }
        
        if(frameSize.getSize() != null) {
            mainWindow.setSize(frameSize.getSize());
        }
        if(frameSize.getLocation() != null) {
            mainWindow.setLocation(frameSize.getLocation());
        }
    }
}
