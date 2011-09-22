package com.mostlymusic.downloader.gui;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;

import javax.swing.*;
import java.awt.*;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 12:40 PM
 */
@Singleton
public class MainContainer {
    private static final String ACCOUNTS = "ACCOUNTS";
    private static final String ITEMS = "ITEMS";

    private JPanel panel1;
    private JList list1;
    private JPanel cardPanel;

    private ApplicationModel model;
    private CardLayout layout;

    @Inject
    public MainContainer(AccountsList accountsList, Items items, ApplicationModel model) {
        layout = (CardLayout) cardPanel.getLayout();
        setAccountsList(accountsList);
        setItems(items);
        layout.show(this.cardPanel, ACCOUNTS);
        this.model = model;
        model.addListener(new ApplicationModelListenerAdapter() {
            @Override
            public void loggedIn(Account account) {
                layout.show(cardPanel, ITEMS);
            }
        });
    }

    public Component getContentPane() {
        return panel1;
    }

    private void setAccountsList(AccountsList accountsList) {
        cardPanel.add(accountsList.getContentPane(), ACCOUNTS);
    }

    private void setItems(Items items) {
        cardPanel.add(items.getContentPane(), ITEMS);
    }
}
