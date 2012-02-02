package com.mostlymusic.downloader;

import com.google.inject.AbstractModule;
import com.mostlymusic.downloader.gui.ApplicationMenuBar;
import com.mostlymusic.downloader.gui.ApplicationModel;
import com.mostlymusic.downloader.gui.ConfigurationDialog;
import com.mostlymusic.downloader.gui.DefaultApplicationModel;
import com.mostlymusic.downloader.gui.JDialogLoginDialog;
import com.mostlymusic.downloader.gui.LoginDialog;
import com.mostlymusic.downloader.gui.MainContainer;
import com.mostlymusic.downloader.gui.MainLayout;
import com.mostlymusic.downloader.gui.MainWindow;

import javax.swing.*;
import javax.swing.plaf.synth.SynthLookAndFeel;
import java.text.ParseException;

/**
 * @author ytaras
 *         Date: 9/19/11
 *         Time: 7:35 PM
 */
public class GuiModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ApplicationModel.class).to(DefaultApplicationModel.class);
        bind(MainContainer.class).annotatedWith(MainLayout.class).to(MainContainer.class);
        bind(ErrorHandlerListener.class).asEagerSingleton();
        bind(JMenuBar.class).to(ApplicationMenuBar.class);
        bind(MainWindow.class);
        bind(LoginDialog.class).to(JDialogLoginDialog.class);
        bind(ConfigurationDialog.class).asEagerSingleton();
        bind(LookAndFeel.class).toInstance(createLookAndFeel());
    }

    private LookAndFeel createLookAndFeel() {
        SynthLookAndFeel synthLookAndFeel = new SynthLookAndFeel();
        try {
            synthLookAndFeel.load(GuiModule.class.getResourceAsStream("/laf.xml"), GuiModule.class);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return synthLookAndFeel;
    }

}
