package com.mostlymusic.downloader.localdata;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mostlymusic.downloader.LocalStorageModule;
import org.junit.Before;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:52 PM
 */
public class StoragetTestBase {

    protected Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new LocalStorageModule());
    }
}
