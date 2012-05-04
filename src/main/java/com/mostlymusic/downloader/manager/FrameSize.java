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
    private Dimension size;
    private Point location;

    public FrameSize(Dimension size) {
        this.size = size;
    }

    @SuppressWarnings("UnusedDeclaration")
    public FrameSize() {
    }

    public FrameSize(Dimension size, Point location) {
        this.size = size;
        this.location = location;
    }

    // For MyBatis
    @SuppressWarnings("UnusedDeclaration")
    public void setFrameSize(String string) {
        if (string == null || string.isEmpty()) {
            size = null;
            location = null;
        } else {
            FrameSize frameSize = GSON.fromJson(string, FrameSize.class);
            this.size = frameSize.size;
            this.location = frameSize.location;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public String getFrameSize() {
        return GSON.toJson(this);
    }

    public Dimension getSize() {
        return size;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("FrameSize");
        sb.append("{dimension=").append(size);
        sb.append('}');
        return sb.toString();
    }

    public Point getLocation() {
        return location;
    }
}
