package com.mostlymusic.downloader.gui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.DownloaderModule;
import com.mostlymusic.downloader.GuiModule;
import com.mostlymusic.downloader.LocalStorageModule;
import com.mostlymusic.downloader.localdata.ConfigurationMapper;
import org.fest.swing.core.EmergencyAbortListener;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.finder.JFileChooserFinder;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author ytaras
 *         Date: 11/10/11
 *         Time: 9:51 AM
 */
public class ConfigurationDialogTest {
    private DialogFixture configDialog;
    private EmergencyAbortListener listener;
    private ConfigurationMapper configurationMapper;

    @Before
    public void setUp() throws Exception {
        listener = EmergencyAbortListener.registerInToolkit();
        final Injector injector = Guice.createInjector(new GuiModule(), new DownloaderModule(""), new LocalStorageModule());
        configurationMapper = injector.getInstance(ConfigurationMapper.class);
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
        configDialog.textBox("downloadLocation").requireText(configurationMapper.getDownloadPath());
        configDialog.button("showChooser").click();
        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(configDialog.robot);
        File newDir = new File(configurationMapper.getDownloadPath()).getParentFile();
        fileChooser.setCurrentDirectory(newDir);
        fileChooser.approve();
        configDialog.button("ok").click();
        assertThat(configurationMapper.getDownloadPath()).isEqualTo(newDir.getAbsolutePath());
    }

    @After
    public void tearDown() throws Exception {
        configDialog.cleanUp();
        listener.unregister();
    }
}
