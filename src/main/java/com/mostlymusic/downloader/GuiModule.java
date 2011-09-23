package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.gui.*;

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
        bind(MainContainer.class).annotatedWith(MainLayout.class).to(MainContainer.class);
        bind(ErrorHandlerListener.class).asEagerSingleton();

        String userHome = System.getProperty("user.home");
        bind(File.class).annotatedWith(DownloadDirectory.class).toInstance(getDownloadsDir(userHome));
    }

    private File getDownloadsDir(String userHome) {
        File downloads = new File(userHome, "Downloads");
        if (!downloads.exists()) {
            downloads.mkdirs();
        }
        return downloads;
    }
}
