package com.mostlymusic.downloader.manager;

import com.google.inject.Injector;
import com.mostlymusic.downloader.MockInjectors;
import org.junit.Before;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 3:52 PM
 */
public class StoragetTestBase {

    static Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = MockInjectors.storageTempDb(false);
    }
}
