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

import io.github.thred.climatetray.ui.ClimateTrayPopupController;
import io.github.thred.climatetray.util.MessageBuffer;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClimateTray
{

    public static final MessageBuffer LOG = new MessageBuffer(true, 1024);

    public static final int TRAY_ICON_SIZE = 16;

    public static final TrayIcon TRAY_ICON = new TrayIcon(ClimateTrayImage.ICON.getImage(
        ClimateTrayImageState.NOT_SELECTED, TRAY_ICON_SIZE));

    public static final ClimateTrayPreferences PREFERENCES = new ClimateTrayPreferences();

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
            LOG.error("Failed to set LookAndFeel", e);
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

        ClimateTrayService.load();
        ClimateTrayService.scheduleUpdate();
    }

    public static void updateTrayIcon(Image image, String toolTip)
    {
        TRAY_ICON.setImage((image != null) ? image : ClimateTrayImage.ICON.getImage(ClimateTrayImageState.NOT_SELECTED,
            TRAY_ICON_SIZE));
        TRAY_ICON.setToolTip((toolTip != null) ? toolTip : "Climate-Tray");
    }

    public static void popup(int x, int y)
    {
        LOG.debug("Opening popup.");

        POPUP_CONTROLLER.consume(x, y);
    }

}
