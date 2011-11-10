package com.mostlymusic.downloader.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.GuiModule;
import com.mostlymusic.downloader.LocalStorageModule;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.DialogFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author ytaras
 *         Date: 11/10/11
 *         Time: 9:51 AM
 */
public class ConfigurationDialogTest {
    private DialogFixture configDialog;

    @Before
    public void setUp() throws Exception {
        final Injector injector = Guice.createInjector(new GuiModule(), new DownloaderModule(""), new LocalStorageModule());
        ConfigurationDialog execute = GuiActionRunner.execute(new GuiQuery<ConfigurationDialog>() {
            @Override
            protected ConfigurationDialog executeInEDT() throws Throwable {
                return injector.getInstance(ConfigurationDialog.class);
            }
        });
        configDialog = new DialogFixture(execute);
        configDialog.show();
    }

    @Test
    public void shouldShowDialog() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        configDialog.cleanUp();
    }
}
