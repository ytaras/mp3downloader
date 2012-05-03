package com.mostlymusic.downloader.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: ytaras
 * Date: 28.04.12
 * Time: 12:43
 */
public class CloseButton extends JButton {

    public CloseButton() {
        setIcon(new ImageIcon(getClass().getResource("/controls/close_button.png")));
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }


}
