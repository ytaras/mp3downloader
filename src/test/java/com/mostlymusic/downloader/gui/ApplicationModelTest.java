package com.mostlymusic.downloader.gui;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author ytaras
 *         Date: 9/20/11
 *         Time: 1:30 PM
 */
public class ApplicationModelTest {

    @Test
    public void shouldInformListenersOnStatusChange() {
        // given
        ApplicationModel model = new DefaultApplicationModel(null, null, null, null, null, null, null, null);
        ApplicationModelListener mock = mock(ApplicationModelListener.class);
        model.addListener(mock);

        // when
        model.setStatus("a");
        model.setStatus(null);

        // then
        verify(mock).statusSet("a");
        verify(mock).statusUnset();
    }
}
