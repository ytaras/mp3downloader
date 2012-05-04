package com.mostlymusic.downloader.gui.worker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;
import javax.swing.*;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.mostlymusic.downloader.manager.ConfigurationMapper;

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
    private Semaphore semaphore = new Semaphore(1);

    @Inject
    public void setInjector(Injector injector) {
        this.injector = injector;
    }

    @Inject
    public void setConfiguration(ConfigurationMapper configuration) {
        this.configuration = configuration;
    }

    CheckServerUpdatesWorker getInstance() {
        return injector.getInstance(CheckServerUpdatesWorker.class);
    }

    public synchronized void schedule() {
        if (null == timer) {
            this.timer = new Timer(0, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (semaphore.tryAcquire()) {
                        getInstance().execute();
                    }
                }
            });
        } else {
            timer.stop();
        }
        this.timer.setDelay(configuration.getRefreshRate() * 60 * 1000);
        timer.setInitialDelay(0);
        timer.start();
    }

    public void done() {
        semaphore.release();
    }
}
