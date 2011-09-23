package com.mostlymusic.downloader.gui.worker;

import com.mostlymusic.downloader.client.exceptions.RequestException;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.LogEvent;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * @author ytaras
 *         Date: 9/22/11
 *         Time: 6:46 PM
 */
public abstract class AbstractSwingClientWorker<K, V> extends SwingWorker<K, V> {

    private ApplicationModel applicationModel;

    protected AbstractSwingClientWorker(ApplicationModel applicationModel) {
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

    protected void beforeGet() {

    }

    protected abstract void doDone(K k);


    protected void processException(Throwable cause) {
        if (cause instanceof RequestException) {
            applicationModel.publishLogStatus(new LogEvent(getErrorMessage(cause), cause));
        }
        getApplicationModel().setStatus(null);
        getApplicationModel().fireExceptionEvent(cause);
    }

    protected String getErrorMessage(Throwable cause) {
        return "Error occurred: ";
    }

    public ApplicationModel getApplicationModel() {
        return applicationModel;
    }
}
