package com.mostlymusic.downloader.gui.worker;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.dto.Item;
import com.mostlymusic.downloader.manager.ArtistMapper;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author ytaras
 */
public class FileDownloaderTest {

    private IDownloadFileWorker downloadFileWorkerMock;
    private Injector injector;
    private FileDownloader fileDownloader;

    @Before
    public void setUp() throws Exception {
        downloadFileWorkerMock = mock(IDownloadFileWorker.class);
        injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(IDownloadFileWorker.class).toInstance(downloadFileWorkerMock);
                bind(ArtistMapper.class).toInstance(mock(ArtistMapper.class));
                bind(FileDownloader.class);
            }
        });
        fileDownloader = injector.getInstance(FileDownloader.class);
    }

    @Test
    public void shouldDownload() throws Exception {
        // given
        Item item = new Item();
        // when
        IDownloadFileWorker worker = fileDownloader.createWorker(item);

        // then
        assertThat(worker).isSameAs(downloadFileWorkerMock);
        verify(worker).setItem(item);
        verifyNoMoreInteractions(worker);
    }

    @Test
    public void shouldScheduleDownload() throws Exception {
        // given
        Item item = new Item();

        // when
        fileDownloader.scheduleDownload(item);

        // then
        verify(downloadFileWorkerMock).setItem(item);
        verify(downloadFileWorkerMock).execute();
        verifyNoMoreInteractions(downloadFileWorkerMock);
    }
}
