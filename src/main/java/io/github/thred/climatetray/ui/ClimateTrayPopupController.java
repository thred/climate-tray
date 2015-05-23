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
package io.github.thred.climatetray.ui;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.Component;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.WindowConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ClimateTrayPopupController extends AbstractClimateTrayController<ClimateTrayPreferences, JPopupMenu>
    implements PopupMenuListener
{

    private final JMenuItem preferencesItem = createMenuItem("Preferences...", null,
        "Manage the presets, air conditioners and other settings.", (e) -> ClimateTray.preferences());
    private final JMenuItem logItem = createMenuItem("Log...", null, null, (e) -> ClimateTray.log());
    private final JMenuItem aboutItem = createMenuItem("About...", null, null, (e) -> ClimateTray.about());
    private final JMenuItem exitItem = createMenuItem("Exit", null, null, (e) -> ClimateTray.exit());
    private final List<Component> dynamicItems = new ArrayList<>();

    private JDialog hiddenDialogForFocusManagement;

    public ClimateTrayPopupController()
    {
        super();
    }

    @Override
    protected JPopupMenu createView()
    {
        JPopupMenu view = new JPopupMenu("Climate Tray");

        view.addPopupMenuListener(this);
        view.add(preferencesItem);
        view.add(logItem);
        view.add(aboutItem);
        view.addSeparator();
        view.add(exitItem);

        return view;
    }

    @Override
    public void prepareWith(ClimateTrayPreferences model)
    {
        JPopupMenu view = getView();
        int index = 0;

        dynamicItems.stream().forEach(view::remove);
        dynamicItems.clear();

        index = preparePresets(model, view, index, model.isAnyDeviceSelected());
        prepareDevices(model, view, index);

    }

    protected int preparePresets(ClimateTrayPreferences model, JPopupMenu view, int index, boolean enabled)
    {
        ButtonGroup group = new ButtonGroup();
        List<MNetPreset> presets = model.getPresets();

        if (presets.size() > 0)
        {
            for (MNetPreset preset : presets)
            {
                Icon icon = createIcon(preset);
                JRadioButtonMenuItem item =
                    SwingUtils.createRadioButtonMenuItem(preset.describe(), icon, null, (e) -> presetSelect(preset));

                item.setName(preset.getId().toString());
                item.setEnabled(enabled);

                view.add(item, index++);
                group.add(item);
                dynamicItems.add(item);
            }

            dynamicItems.add(view.add(new JPopupMenu.Separator(), index++));
        }
        return index;
    }

    protected int prepareDevices(ClimateTrayPreferences model, JPopupMenu view, int index)
    {
        List<MNetDevice> devices = model.getDevices();

        if (devices.size() > 0)
        {
            for (MNetDevice device : devices)
            {
                Icon icon = createIcon(device);
                JCheckBoxMenuItem item =
                    SwingUtils.createCheckBoxMenuItem(device.describeState(), icon, device.describeState(),
                        (e) -> deviceSelect(device));

                item.setName(device.getId().toString());
                item.setSelected(device.isSelected());

                dynamicItems.add(view.add(item, index++));
            }

            dynamicItems.add(view.add(new JPopupMenu.Separator(), index++));
        }

        return index;
    }

    protected Icon createIcon(MNetPreset preset)
    {
        ClimateTrayImageState imageState =
            (preset.isSelected()) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED;

        return preset.createIcon(imageState, 16);
    }

    protected Icon createIcon(MNetDevice device)
    {
        ClimateTrayImageState imageState =
            (device.isSelected()) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED;

        return device.getState().createIcon(imageState, 16);
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
        // intentionally left blank
    }

    public void consume()
    {
        Point location = MouseInfo.getPointerInfo().getLocation();

        consume(location.x, location.y);
    }

    public void consume(int x, int y)
    {
        JPopupMenu view = getView();

        if (view.isVisible())
        {
            view.setVisible(false);
        }

        monitor.block();

        try
        {
            prepareWith(ClimateTray.PREFERENCES);
        }
        finally
        {
            monitor.unblock();
        }

        hiddenDialogForFocusManagement = new JDialog((Frame) null, "Climate Tray");

        hiddenDialogForFocusManagement.setUndecorated(true);
        hiddenDialogForFocusManagement.setIconImages(ClimateTrayImage.ICON.getImages(ClimateTrayImageState.NONE, 64,
            48, 32, 24, 16));
        hiddenDialogForFocusManagement.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        hiddenDialogForFocusManagement.setVisible(true);

        view.pack();
        view.show(hiddenDialogForFocusManagement, x, y);
    }

    public void presetSelect(MNetPreset preset)
    {
        ClimateTray.togglePreset(preset.getId());
    }

    public void deviceSelect(MNetDevice device)
    {
        ClimateTray.toggleDevice(device.getId());
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e)
    {
        // intentionally left blank
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
    {
        if (hiddenDialogForFocusManagement != null)
        {
            hiddenDialogForFocusManagement.setVisible(false);
            hiddenDialogForFocusManagement.dispose();

            hiddenDialogForFocusManagement = null;
        }
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e)
    {
        // intentionally left blank
    }

}
