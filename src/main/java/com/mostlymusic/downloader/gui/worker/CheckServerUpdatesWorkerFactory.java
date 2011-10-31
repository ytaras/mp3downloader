package com.mostlymusic.downloader.gui.worker;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.dto.Account;
import com.mostlymusic.downloader.localdata.ConfigurationMapper;

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
    private ConfigurationMapper configuration;
    private Timer timer;
    private Account account;

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Inject
    public void setConfiguration(ConfigurationMapper configuration) {
        this.configuration = configuration;
    }

    public CheckServerUpdatesWorker getInstance(Account account) {
        CheckServerUpdatesWorker instance = injector.getInstance(CheckServerUpdatesWorker.class);
        instance.setAccount(account);
        return instance;
    }

    public synchronized void schedule(final Account account) {
        if (null == timer) {
            this.timer = new Timer(0, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getInstance(CheckServerUpdatesWorkerFactory.this.account).execute();
                }
            });
        } else {
            timer.stop();
        }
        this.account = account;
        this.timer.setDelay(configuration.getRefreshRate() * 60 * 1000);
        timer.setInitialDelay(0);
        timer.start();
    }
}
