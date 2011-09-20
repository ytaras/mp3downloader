package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;

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
    private ApplicationModel model;

    @Inject
    public MainPanel(AccountsList accountsList, Items items, ApplicationModel model) {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setAccountsList(accountsList);
        setItems(items);
        cardLayout.show(this, ACCOUNTS);
        this.model = model;
        model.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(Account account) {
                cardLayout.show(MainPanel.this, ITEMS);
            }
        });
    }

    private void setAccountsList(AccountsList accountsList) {
        add(accountsList.getContentPane(), ACCOUNTS);
    }

    private void setItems(Items items) {
        add(items.getContentPane(), ITEMS);
    }
}
