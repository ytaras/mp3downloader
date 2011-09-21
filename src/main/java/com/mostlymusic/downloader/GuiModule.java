package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.gui.*;

import javax.swing.*;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:35 PM
 */
public class GuiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AccountsList.class);
        bind(ApplicationModel.class).to(DefaultApplicationModel.class);
        bind(JPanel.class).annotatedWith(MainLayout.class).to(MainPanel.class);
        bind(ErrorHandlerListener.class).asEagerSingleton();
    }
}
