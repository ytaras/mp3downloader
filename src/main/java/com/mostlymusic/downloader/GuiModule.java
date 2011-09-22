package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.gui.*;

import javax.swing.*;
import java.io.File;

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

        String userHome = System.getProperty("user.home");
        bind(File.class).annotatedWith(DownloadDirectory.class).toInstance(new File(userHome, "Downloads"));
    }
}
