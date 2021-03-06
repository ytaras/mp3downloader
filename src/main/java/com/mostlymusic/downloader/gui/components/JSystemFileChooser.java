package com.mostlymusic.downloader.gui.components;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import sun.swing.FilePane;

/**
 * Created with IntelliJ IDEA.
 * User: ytaras
 * Date: 28.04.12
 * Time: 18:17
 */
public class JSystemFileChooser extends JFileChooser {
    public void updateUI() {
        LookAndFeel old = UIManager.getLookAndFeel();
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("mac")) {
                // We are on Mac - we have to show some Swing FileChooser(because AWT can't select dirs)
                // but systemLookAndFeel for mac has a bug
                UIManager.setLookAndFeel(new MetalLookAndFeel());
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }

        } catch (Throwable ex) {
            old = null;
        }

        super.updateUI();

        if (old != null) {
            FilePane filePane = findFilePane(this);
            if (filePane != null) {
                filePane.setViewType(FilePane.VIEWTYPE_DETAILS);
                filePane.setViewType(FilePane.VIEWTYPE_LIST);
            }

            Color background = UIManager.getColor("Label.background");
            setBackground(background);
            setOpaque(true);

            try {
                UIManager.setLookAndFeel(old);
            } catch (UnsupportedLookAndFeelException ignored) {
            } // shouldn't get here
        }
    }

    private static FilePane findFilePane(Container parent) {
        for (Component comp : parent.getComponents()) {
            if (FilePane.class.isInstance(comp)) {
                return (FilePane) comp;
            }
            if (comp instanceof Container) {
                Container cont = (Container) comp;
                if (cont.getComponentCount() > 0) {
                    FilePane found = findFilePane(cont);
                    if (found != null) {
                        return found;
                    }
                }
            }
        }
        return null;
    }
}
