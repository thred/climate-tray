/*
 * Copyright 2015 Manfred Hantschel
 * 
 * This file is part of Climate-Tray.
 * 
 * Climate-Tray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Climate-Tray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Climate-Tray. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package io.github.thred.climatetray;

import io.github.thred.climatetray.controller.ClimateTrayAboutDialogController;
import io.github.thred.climatetray.controller.ClimateTrayPopupController;
import io.github.thred.climatetray.controller.ClimateTrayPreferencesDialogController;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.prefs.SystemPrefs;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClimateTray
{

    private static final SystemPrefs PREFS = SystemPrefs.get(ClimateTray.class);

    public static final ClimateTrayPreferences PREFERENCES = new ClimateTrayPreferences();

    private static final Timer TIMER = new Timer();

    private static final TrayIcon TRAY_ICON = new TrayIcon(ClimateTrayImage.ICON.getImage(
        ClimateTrayImageState.NOT_SELECTED, 16));
    private static final ClimateTrayPopupController POPUP_CONTROLLER;

    static
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException
            | UnsupportedLookAndFeelException e)
        {
            e.printStackTrace(System.err);
        }

        POPUP_CONTROLLER = new ClimateTrayPopupController();
    }

    public static void main(String[] arguments)
    {
        if (!SystemTray.isSupported())
        {
            throw new UnsupportedOperationException("SystemTray is not supported");
        }

        TRAY_ICON.setImageAutoSize(true);
        TRAY_ICON.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    popup(e.getX(), e.getY());
                }
            }
        });

        SystemTray tray = SystemTray.getSystemTray();

        try
        {
            tray.add(TRAY_ICON);
        }
        catch (AWTException e)
        {
            throw new ClimateTrayException("TrayIcon could not be added.", e);
        }

        load();

        start();
    }

    public static void load()
    {
        try
        {
            PREFERENCES.read(PREFS);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);

            ClimateTrayUtils.errorDialog("Preferences", "Failed to load preferences.");
        }
    }

    public static void store()
    {
        PREFERENCES.write(PREFS);
    }

    public static void start()
    {
        TIMER.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    update();
                }
                catch (Throwable e)
                {
                    e.printStackTrace(System.err);
                }
            }
        }, 0, PREFS.getLong("update-schedule-in-minutes", 1l) * 60 * 1000);
    }

    public static void stop()
    {
        TIMER.cancel();
    }

    public static void popup(int x, int y)
    {
        POPUP_CONTROLLER.consume(x, y);
    }

    public static void update()
    {
        System.out.println("Implement update");
    }

    public static void preferences()
    {
        ClimateTrayPreferencesDialogController controller = new ClimateTrayPreferencesDialogController();

        controller.consume(null, PREFERENCES);
    }

    public static void togglePreset(UUID id)
    {
        PREFERENCES.getPresets().stream().forEach((preset) -> preset.setEnabled(false));

        MNetPreset preset = PREFERENCES.getPreset(id);

        if (preset == null)
        {
            return;
        }

        // TODO
        preset.setEnabled(true);

        System.out.println(preset);
    }

    public static void toggleDevice(UUID id)
    {
        MNetDevice device = PREFERENCES.getDevice(id);

        if (device == null)
        {
            return;
        }

        device.setEnabled(!device.isEnabled());
        store();
    }

    public static void about()
    {
        new ClimateTrayAboutDialogController().consume(null, PREFERENCES);
    }

    public static void exit()
    {
        stop();

        SystemTray tray = SystemTray.getSystemTray();

        tray.remove(TRAY_ICON);

        System.exit(0);
    }

}
