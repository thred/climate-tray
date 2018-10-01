/*
 * Copyright 2015 - 2018 Manfred Hantschel
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetState;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class ClimateTrayWindowController extends AbstractClimateTrayController<ClimateTrayPreferences, JFrame>
{

    public static final int ICON_SIZE = 32;

    private final ClimateTrayPopupController popupController = new ClimateTrayPopupController();
    private final JFrame view;
    private final JPanel panel;
    private final JLabel icon;
    private final JLabel text;
    private final JLabel settings;

    private Point dragging = null;

    public ClimateTrayWindowController()
    {
        super();

        view = new JFrame("Climate Tray");

        view.setIconImages(ClimateTrayImage.ICON.getImages(ClimateTrayImageState.DEFAULT, 64, 48, 32, 24, 16));
        view.setUndecorated(true);
        view.setBackground(Color.WHITE);
        view.setSize(256, ICON_SIZE);

        view.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    Point location = e.getPoint();

                    popup(location.x, location.y);
                }
                else
                {
                    dragging = e.getLocationOnScreen();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    Point location = e.getLocationOnScreen();

                    popup(location.x, location.y);
                }
                else if (dragging != null)
                {
                    ClimateTray.PREFERENCES.setWindowLocation(view.getLocation());
                    ClimateTrayService.store();
                    dragging = null;
                }
            }
        });

        view.addMouseMotionListener(new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                if (dragging != null)
                {
                    Point point = e.getLocationOnScreen();
                    Point location = view.getLocation();

                    location.x -= dragging.x - point.x;
                    location.y -= dragging.y - point.y;

                    dragging = point;

                    view.setLocation(location);
                }
            }
        });

        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(0x202030)));

        view.setLayout(new BorderLayout());
        view.add(panel, BorderLayout.CENTER);

        icon = SwingUtils
            .createClickLabelIcon(ClimateTrayImage.ICON.getIcon(ClimateTrayImageState.DEFAULT, ICON_SIZE),
                "Climate Tray", event -> adjust());
        panel.add(icon, BorderLayout.WEST);

        text = SwingUtils.createLabel("Climate Tray");
        text.setHorizontalAlignment(SwingConstants.CENTER);
        text.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        panel.add(text, BorderLayout.CENTER);

        settings = SwingUtils
            .createClickLabelIcon(ClimateTrayImage.SETTINGS.getIcon(ClimateTrayImageState.DEFAULT, ICON_SIZE),
                "Settings", event -> popup(0, 0));
        panel.add(settings, BorderLayout.EAST);
    }

    @Override
    protected JFrame createView()
    {
        view.setVisible(true);

        return view;
    }

    @Override
    public void prepareWith(ClimateTrayPreferences model)
    {
        super.prepareWith(model);

        SwingUtilities.invokeLater(() -> {
            getView().setLocation(model.getWindowLocation());
            SwingUtils.fixLocation(view);
        });
    }

    @Override
    public void refreshWith(ClimateTrayPreferences model)
    {
        MNetDevice activeDevice = model
            .getDevices()
            .stream()
            .filter(device -> (device.isEnabled()) && (device.isSelectedAndWorking()))
            .findFirst()
            .orElse(null);

        if (activeDevice != null)
        {
            MNetState state = activeDevice.getState();

            view.setIconImages(state.createImages(ClimateTrayImageState.DEFAULT, 64, 48, 32, 24, 16));
            icon.setIcon(state.createIcon(ClimateTrayImageState.DEFAULT, ICON_SIZE));
            text.setText(activeDevice.getState().describeActionShort());
        }
        else
        {
            view.setIconImages(ClimateTrayImage.ICON.getImages(ClimateTrayImageState.DEFAULT, 64, 48, 32, 24, 16));
            icon.setIcon(ClimateTrayImage.ICON.getIcon(ClimateTrayImageState.DEFAULT, ICON_SIZE));
            text.setText("Climate Tray");
        }

        //if (popupController.getView().isVisible())
        //{
        popupController.refreshWith(model);
        //}

        view.revalidate();
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
        view.setVisible(false);

        popupController.dismiss(model);
    }

    public void adjust()
    {
        LOG.debug("Opening adjust.");

        ClimateTrayService.adjustAll();
    }

    public void popup(int x, int y)
    {
        LOG.debug("Opening popup.");

        popupController.consume(view, x, y);
    }

}
