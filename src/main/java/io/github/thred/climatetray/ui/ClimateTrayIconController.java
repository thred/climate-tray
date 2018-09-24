/*
 * Copyright 2015, 2016 Manfred Hantschel
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
package io.github.thred.climatetray.ui;

import static io.github.thred.climatetray.ClimateTray.*;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.util.message.MessageBuffer;

public class ClimateTrayIconController extends AbstractClimateTrayController<ClimateTrayPreferences, TrayIcon>
{

    public static final int TRAY_ICON_SIZE = 16;

    private final ClimateTrayPopupController popupController = new ClimateTrayPopupController();
    private final TrayIcon view =
        new TrayIcon(ClimateTrayImage.ICON.getImage(ClimateTrayImageState.DEFAULT, TRAY_ICON_SIZE));

    public ClimateTrayIconController()
    {
        super();

        view.setImageAutoSize(true);
        view.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    popup(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    popup(e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    protected TrayIcon createView()
    {
        return view;
    }

    @Override
    public void refreshWith(ClimateTrayPreferences model)
    {
        if (!SystemTray.isSupported())
        {
            throw new UnsupportedOperationException("SystemTray is not supported");
        }

        SystemTray tray = SystemTray.getSystemTray();
        boolean enabled = model.isTrayIconEnabled();

        if (enabled)
        {
            MNetDevice activeDevice = model
                .getDevices()
                .stream()
                .filter(device -> (device.isEnabled()) && (device.isSelectedAndWorking()))
                .findFirst()
                .orElse(null);

            if (activeDevice != null)
            {
                Image image = activeDevice.getState().createImage(ClimateTrayImageState.DEFAULT, TRAY_ICON_SIZE);
                String toolTip = activeDevice.describeStateAction();

                refreshIconWith(image, toolTip);
            }
            else
            {
                refreshIconWith(null, null);
            }
        }
        else
        {
            refreshIconWith(null, null);
        }

        boolean exists = Arrays.stream(tray.getTrayIcons()).filter(trayIcon -> trayIcon == view).count() > 0;

        if ((enabled) && (!exists))
        {
            try
            {
                tray.add(view);
            }
            catch (AWTException e)
            {
                LOG.error("TrayIcon could not be added.", e);

                model.setTrayIconEnabled(false);
                ClimateTrayService.store();
            }
        }
        else if ((!enabled) && (exists))
        {
            tray.remove(view);
        }

        //if (popupController.getView().isVisible())
        //{
        popupController.refreshWith(model);
        //}
    }

    protected void refreshIconWith(Image image, String toolTip)
    {
        view
            .setImage((image != null) ? image
                : ClimateTrayImage.ICON.getImage(ClimateTrayImageState.DEFAULT, TRAY_ICON_SIZE));
        view.setToolTip((toolTip != null) ? toolTip : "Climate-Tray");
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        // intentionally left blank
    }

    @Override
    public void applyTo(ClimateTrayPreferences model)
    {
        // intentionally left blank
    }

    @Override
    public void dismiss(ClimateTrayPreferences model)
    {
        SystemTray.getSystemTray().remove(view);

        popupController.dismiss(model);
    }

    public void popup(int x, int y)
    {
        LOG.debug("Opening popup.");

        popupController.consume(x, y);
    }

}
