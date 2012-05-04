package com.mostlymusic.downloader.manager;

import java.awt.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * (C) Copyright 2002-2011 Hewlett-Packard Development Company, L.P.
 * @author ytaras
 */
public class FrameSize {
    private static final Gson GSON = new GsonBuilder().create();
    private Dimension dimension;

    public FrameSize(Dimension dimension) {
        this.dimension = dimension;
    }

    public FrameSize() {
    }

    // For MyBatis
    public void setFrameSize(String string) {
        this.dimension = GSON.fromJson(string, Dimension.class);
    }

    public String getFrameSize() {
        return GSON.toJson(dimension);
    }

    public Dimension getDimension() {
        return dimension;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FrameSize");
        sb.append("{dimension=").append(dimension);
        sb.append('}');
        return sb.toString();
    }
}
