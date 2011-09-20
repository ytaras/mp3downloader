package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.*;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 3:43 PM
 */
@Singleton
public class MainPanel extends JPanel {
    private static final String ACCOUNTS = "ACCOUNTS";
    private static final String ITEMS = "ITEMS";
    private CardLayout cardLayout;

    @Inject
    public MainPanel(AccountsList accountsList, Items items) {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setAccountsList(accountsList);
        setItems(items);
        cardLayout.show(this, ACCOUNTS);
    }

    private void setAccountsList(AccountsList accountsList) {
        add(accountsList.getContentPane(), ACCOUNTS);
    }

    private void setItems(Items items) {
        add(items.getContentPane(), ITEMS);
    }
}
