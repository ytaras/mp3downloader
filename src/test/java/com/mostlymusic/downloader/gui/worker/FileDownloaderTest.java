package com.mostlymusic.downloader.gui.worker;

import java.beans.PropertyChangeListener;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.manager.ArtistMapper;
import com.mostlymusic.downloader.manager.ConfigurationMapper;
import com.mostlymusic.downloader.manager.ItemManager;
import com.mostlymusic.downloader.manager.ProductMapper;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
        when(configurationMapper.getDownloadThreadsNumber()).thenReturn(2);
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

    @Test
    public void shouldScheduleOnlyOnce() {
        // given
        Item item = new Item(1);
        Item item2 = new Item(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                Thread.sleep(1000);
                return null;
            }
        }).when(downloadFileWorkerMock).execute();

        // when
        fileDownloader.scheduleDownload(item, null);
        fileDownloader.scheduleDownload(item2, null);
        Assertions.assertThat(fileDownloader.isDownloading(item) || fileDownloader.isScheduled(item)).isTrue();

        // then
        verify(configurationMapper, times(1)).getDownloadThreadsNumber();
        verify(downloadFileWorkerMock, times(1)).setDownloadData(item, null);
        verify(downloadFileWorkerMock, times(1)).execute();
        verify(downloadFileWorkerMock, times(1)).addPropertyChangeListener(any(PropertyChangeListener.class));
        verifyNoMoreInteractions(downloadFileWorkerMock);
    }


}
