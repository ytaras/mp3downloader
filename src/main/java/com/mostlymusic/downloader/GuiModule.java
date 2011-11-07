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
        bind(MainContainer.class).annotatedWith(MainLayout.class).to(MainContainer.class);
        bind(ErrorHandlerListener.class).asEagerSingleton();
        bind(JMenuBar.class).to(ApplicationMenuBar.class);
        bind(MainWindow.class);
        bind(ILoginDialog.class).to(LoginDialog.class);
    }

}
