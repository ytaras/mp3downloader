package com.mostlymusic.downloader.gui;

import java.io.File;
import javax.swing.*;

import com.mostlymusic.downloader.manager.ConfigurationMapper;
import org.fest.swing.core.EmergencyAbortListener;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.finder.JFileChooserFinder;
import org.fest.swing.fixture.DialogFixture;
import org.fest.swing.fixture.JCheckBoxFixture;
import org.fest.swing.fixture.JFileChooserFixture;
import org.fest.util.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        JDialog jDialog = new JDialog();
        listener = EmergencyAbortListener.registerInToolkit();
        configurationMapper = mock(ConfigurationMapper.class);
        String tempFolder = Files.newTemporaryFolder().getAbsolutePath();
        when(configurationMapper.getDownloadPath()).thenReturn(tempFolder);
        when(configurationMapper.getRefreshRate()).thenReturn(10);
        when(configurationMapper.getAutoDownload()).thenReturn(true);
        when(configurationMapper.getDownloadThreadsNumber()).thenReturn(3);
        ConfigurationDialog execute = GuiActionRunner.execute(new GuiQuery<ConfigurationDialog>() {
            @Override
            protected ConfigurationDialog executeInEDT() throws Throwable {
                return new ConfigurationDialog(configurationMapper, mock(ApplicationModel.class));
            }
        });
        jDialog.getContentPane().add(execute.getContentPane());
        configDialog = new DialogFixture(jDialog);
        execute.setFrame(mock(MainContainer.class));
        configDialog.show();
    }

    @Test
    public void shouldReadAndWriteConfig() throws Exception {
        // verify initial values
        configDialog.textBox("downloadLocation").requireText(configurationMapper.getDownloadPath());
        configDialog.spinner("refreshRate").requireValue(configurationMapper.getRefreshRate());
        JCheckBoxFixture autoDownload = configDialog.checkBox("autoDownload");
        if (configurationMapper.getAutoDownload()) {
            autoDownload.requireSelected();
        } else {
            autoDownload.requireNotSelected();
        }
        configDialog.spinner("downloadsNumber").requireValue(configurationMapper.getDownloadThreadsNumber());
        // select directory
        configDialog.button("showChooserButton").click();
        JFileChooserFixture fileChooser = JFileChooserFinder.findFileChooser().using(configDialog.robot);
        File newDir = new File(configurationMapper.getDownloadPath()).getParentFile();
        fileChooser.setCurrentDirectory(newDir);
        fileChooser.approve();


        configDialog.spinner("refreshRate").enterText("44");
        configDialog.spinner("downloadsNumber").enterText("5");
        autoDownload.check();

        // save
        configDialog.button("okButton").click();
        verify(configurationMapper).setDownloadPath(newDir.getAbsolutePath());
        verify(configurationMapper).setRefreshRate(44);
        verify(configurationMapper).setDownloadThreadsNumber(5);
        verify(configurationMapper).setAutoDownload(true);
    }

    @Test
    public void shouldNotAllowInvalidValue() {
        configDialog.spinner("refreshRate").enterText("123");
        configDialog.spinner("downloadsNumber").enterText("11");
        configDialog.spinner("refreshRate").enterText("123");
        configDialog.click();
        configDialog.spinner("refreshRate").requireValue(configurationMapper.getRefreshRate());
        configDialog.spinner("downloadsNumber").requireValue(configurationMapper.getDownloadThreadsNumber());

    }


    @After
    public void tearDown() throws Exception {
        configDialog.cleanUp();
        listener.unregister();
    }
}
