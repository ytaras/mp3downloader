package com.mostlymusic.downloader;

import com.google.inject.*;
import com.google.inject.spi.DefaultElementVisitor;
import com.google.inject.spi.Element;
import com.google.inject.spi.Elements;
import com.google.inject.util.Modules;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ytaras
 *         Date: 11/10/11
 *         Time: 10:51 AM
 */
public class MockInjectors {
    private static Module FULL_TEMP_DB_MODULE = Modules.override(new GuiModule(),
            new DownloaderModule(""), new LocalStorageModule()).with(new TempDbModule());
    private static final Module STORAGE_TEMP_DB_MODULE = Modules.override(new LocalStorageModule())
            .with(new TempDbModule());
    private static final Injector STORAGE_TEMP_DB = Guice.createInjector(STORAGE_TEMP_DB_MODULE);

    public static Injector storageTempDb(boolean createNew) {
        if (createNew) {
            return Guice.createInjector(STORAGE_TEMP_DB_MODULE);
        } else {
            return STORAGE_TEMP_DB;
        }
    }

    public static Injector downloader(String serverUrl) {
        return Guice.createInjector(new DownloaderModule(serverUrl));
    }

    private static Module subtractBinding(Module module, final Key<?>... toSubtract) {
        List<Element> elements = Elements.getElements(module);

        List<Element> result = new LinkedList<Element>();
        for (Element element : elements) {
            boolean remove = element.acceptVisitor(new DefaultElementVisitor<Boolean>() {
                @Override
                public <T> Boolean visit(Binding<T> binding) {
                    for (Key<?> key : toSubtract) {
                        if (binding.getKey().equals(key)) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public Boolean visitOther(Element other) {
                    return false;
                }
            });
            if (!remove) {
                result.add(element);
            }
        }

        return Elements.getModule(result);
    }
}
