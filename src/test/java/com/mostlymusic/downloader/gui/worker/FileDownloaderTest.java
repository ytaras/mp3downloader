package com.mostlymusic.downloader.gui.worker;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.manager.ArtistMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author ytaras
 */
public class FileDownloaderTest {

    private DownloadFileWorker downloadFileWorkerMock;
    private FileDownloader fileDownloader;

    @Before
    public void setUp() throws Exception {
        downloadFileWorkerMock = mock(DownloadFileWorker.class);
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(DownloadFileWorker.class).toInstance(downloadFileWorkerMock);
                bind(ArtistMapper.class).toInstance(mock(ArtistMapper.class));
                bind(FileDownloader.class);
            }
        });
        fileDownloader = injector.getInstance(FileDownloader.class);
    }

    @Test
    @Ignore
    public void shouldDownload() throws Exception {
        // given
        Item item = new Item();
        // when
        fileDownloader.scheduleDownload(item);

        // then
        verify(downloadFileWorkerMock).setDownloadData(item, null);
        verifyNoMoreInteractions(downloadFileWorkerMock);
    }

    @Test
    @Ignore
    public void shouldScheduleDownload() throws Exception {
        // given
        Item item = new Item();

        // when
        fileDownloader.scheduleDownload(item, null);

        // then
        verify(downloadFileWorkerMock).setDownloadData(item, null);
        verify(downloadFileWorkerMock).execute();
        verifyNoMoreInteractions(downloadFileWorkerMock);
    }
}
