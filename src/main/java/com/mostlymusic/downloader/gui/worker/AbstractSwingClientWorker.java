package com.mostlymusic.downloader.gui.worker;

import java.util.concurrent.ExecutionException;
import javax.swing.*;

import com.mostlymusic.downloader.client.exceptions.RequestException;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 6:46 PM
 */
public abstract class AbstractSwingClientWorker<K, V> extends SwingWorker<K, V> {

    private final ApplicationModel applicationModel;

    AbstractSwingClientWorker(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
    }

    @Override
    protected final void done() {
        try {
            beforeGet();
            K k = this.get();
            doDone(k);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            processException(e.getCause());
        }
    }

    void beforeGet() {

    }

    protected abstract void doDone(K k);


    void processException(Throwable cause) {
        if (cause instanceof RequestException) {
            applicationModel.publishLogStatus(new LogEvent(getErrorMessage(), cause));
        }
        getApplicationModel().setStatus(null);
        getApplicationModel().fireExceptionEvent(cause);
    }

    String getErrorMessage() {
        return "Error occurred: ";
    }

    ApplicationModel getApplicationModel() {
        return applicationModel;
    }
}
