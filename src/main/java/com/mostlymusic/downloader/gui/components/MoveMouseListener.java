package com.mostlymusic.downloader.gui.components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;

public class MoveMouseListener implements MouseListener, MouseMotionListener {
    JComponent target;
    Point start_drag;
    Point start_loc;

    public MoveMouseListener(JComponent target) {
        this.target = target;
    }

    Point getScreenLocation(MouseEvent e) {
        Point cursor = e.getPoint();
        try {
            Point target_location = this.target.getLocationOnScreen();
            return new Point((int) (target_location.getX() + cursor.getX()),
                    (int) (target_location.getY() + cursor.getY()));
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.err);
        }
        return null;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        this.start_drag = this.getScreenLocation(e);
        this.start_loc = SwingUtilities.getRoot(this.target).getLocation();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        Point current = this.getScreenLocation(e);
        if (current == null) {
            return;
        }
        try {
            Point offset = new Point((int) current.getX() - (int) start_drag.getX(),
                    (int) current.getY() - (int) start_drag.getY());
            Window frame = (Window) SwingUtilities.getRoot(target);
            Point new_location = new Point(
                    (int) (this.start_loc.getX() + offset.getX()), (int) (this.start_loc
                    .getY() + offset.getY()));
            frame.setLocation(new_location);
        } catch (RuntimeException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void mouseMoved(MouseEvent e) {
    }
}
