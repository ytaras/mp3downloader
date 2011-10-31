package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author ytaras
 *         Date: 10/31/11
 *         Time: 9:30 PM
 */
@Singleton
public class CheckServerUpdatesWorkerFactory {

    private Injector injector;

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    public CheckServerUpdatesWorker getInstance(Account account) {
        CheckServerUpdatesWorker instance = injector.getInstance(CheckServerUpdatesWorker.class);
        instance.setAccount(account);
        return instance;
    }

    public synchronized void schedule(final Account account) {
        Timer timer = new Timer(30 * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getInstance(account).execute();
            }
        });
        timer.setInitialDelay(0);
        timer.start();
    }
}
