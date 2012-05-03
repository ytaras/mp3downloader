package com.mostlymusic.downloader.gui.worker;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.manager.ArtistMapper;
import com.mostlymusic.downloader.manager.ConfigurationMapper;
import com.mostlymusic.downloader.manager.ItemManager;
import com.mostlymusic.downloader.manager.ProductMapper;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author ytaras
 */
public class FileDownloaderTest {

    private DownloadFileWorker downloadFileWorkerMock;
    private FileDownloader fileDownloader;
    private ConfigurationMapper configurationMapper;

    @Before
    public void setUp() throws Exception {
        downloadFileWorkerMock = mock(DownloadFileWorker.class);
        configurationMapper = mock(ConfigurationMapper.class);
        when(configurationMapper.getDownloadThreadsNumber()).thenReturn(1);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ArtistMapper.class).toInstance(mock(ArtistMapper.class));
                bind(FileDownloader.class);
                bind(ApplicationModel.class).toInstance(mock(ApplicationModel.class));
                bind(ConfigurationMapper.class).toInstance(configurationMapper);
                bind(ItemManager.class).toInstance(mock(ItemManager.class));
                bind(ProductMapper.class).toInstance(mock(ProductMapper.class));
                bind(DownloadFileWorker.class).toInstance(downloadFileWorkerMock);
            }
        });
        fileDownloader = injector.getInstance(FileDownloader.class);
    }

    @Test
    public void shouldScheduleDownload() throws Exception {
        // given
        Item item = new Item();

        // when
        fileDownloader.scheduleDownload(item, null);

        // then
        verify(configurationMapper).getDownloadThreadsNumber();
        verify(downloadFileWorkerMock).setDownloadData(item, null);
        verify(downloadFileWorkerMock).execute();
    }
}
