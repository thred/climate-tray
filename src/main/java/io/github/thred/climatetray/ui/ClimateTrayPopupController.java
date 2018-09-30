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
import static io.github.thred.climatetray.util.swing.SwingUtils.*;

import java.awt.Component;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Window;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.ui.MNetStatePanel;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.BorderPanel;
import io.github.thred.climatetray.util.swing.TitlePanel;

public class ClimateTrayPopupController extends AbstractClimateTrayController<ClimateTrayPreferences, JPopupMenu>
    implements PopupMenuListener
{

    private final TitlePanel titlePanel = new TitlePanel("Climate Tray", null);
    private final JMenuItem preferencesItem = createMenuItem("Preferences...", null,
        "Manage the presets, air conditioners and other settings.", (e) -> ClimateTrayService.preferences());
    private final JMenuItem logItem = createMenuItem("Log...", null, null, (e) -> ClimateTrayService.log());
    private final JMenuItem aboutItem = createMenuItem("About...", null, null, (e) -> ClimateTrayService.about());
    private final JMenuItem closeItem = createMenuItem("Close Options", null, null, (e) -> {
    });
    private final JMenuItem exitItem = createMenuItem("Exit", null, null, (e) -> ClimateTrayService.exit());
    private final Map<String, Component> dynamicItems = new HashMap<>();

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

        view.add(titlePanel);
        view.add(preferencesItem);
        view.add(logItem);
        view.add(aboutItem);
        view.addSeparator();
        view.add(closeItem);
        view.add(exitItem);

        return view;
    }

    @Override
    public void prepareWith(ClimateTrayPreferences model)
    {
        JPopupMenu view = getView();
        int index = 1;

        dynamicItems.values().stream().forEach(view::remove);
        dynamicItems.clear();

        index = prepareWithPresets(model, view, index, model.isAnyDeviceSelected());
        index = prepareWithDevices(model, view, index);

        JComponent headline = createMenuHeadline("Options", index > 1, true);

        dynamicItems.put("#optionsHeadline", headline);

        view.add(headline, index++);
        view.revalidate();
    }

    protected int prepareWithPresets(ClimateTrayPreferences model, JPopupMenu view, int index, boolean enabled)
    {
        List<MNetPreset> presets = model.getPresets();

        if (presets.size() > 0)
        {
            JComponent headline = createMenuHeadline("Global Presets", index > 1, true);

            dynamicItems.put("#presetHeadline", headline);
            view.add(headline, index++);

            for (MNetPreset preset : presets)
            {
                view.add(prepareWithPreset(preset, enabled), index++);
            }

            //dynamicItems.put("#presetSeparator", view.add(new JPopupMenu.Separator(), index++));
        }

        return index;
    }

    protected JCheckBoxMenuItem prepareWithPreset(MNetPreset preset, boolean enabled)
    {
        String id = preset.getId().toString();
        JCheckBoxMenuItem item = new JCheckBoxMenuItem();

        item.setName(id);
        item.addActionListener((e) -> presetSelect(preset));

        refreshPresetWith(item, preset, enabled);

        dynamicItems.put(id, item);

        return item;
    }

    protected int prepareWithDevices(ClimateTrayPreferences model, JPopupMenu view, int index)
    {
        List<MNetDevice> devices = model.getDevices();

        if (devices.size() > 0)
        {
            JComponent headline = createMenuHeadline("Air Conditioners", index > 1, true);

            dynamicItems.put("#deviceHeadline", headline);
            view.add(headline, index++);

            for (MNetDevice device : devices)
            {
                JMenu menu = prepareWithDevice(device);

                view.add(menu, index++);
            }

            // dynamicItems.put("#deviceSeparator", view.add(new JPopupMenu.Separator(), index++));
        }

        return index;
    }

    protected JMenu prepareWithDevice(MNetDevice device)
    {
        String id = device.getId().toString();
        JMenu menu = new JMenu();

        menu.setName(id);

        dynamicItems.put(id, menu);

        MNetStatePanel titlePanel = new MNetStatePanel(device);
        BorderPanel titleBorder = new BorderPanel(BorderFactory.createEmptyBorder(0, 0, 2, 0), titlePanel);

        dynamicItems.put(id + "#deviceTitle", titlePanel);
        dynamicItems.put(id + "#deviceTitleBorder", menu.add(titleBorder));

        JCheckBoxMenuItem selectItem = new JCheckBoxMenuItem();

        selectItem.addActionListener(e -> deviceSelect(device));
        refreshDeviceSelectWith(selectItem, device);

        dynamicItems.put(id + "#deviceSelect", menu.add(selectItem));

        List<MNetPreset> presets = device.getPresets();

        if (!presets.isEmpty())
        {
            menu.addSeparator();

            int subIndex = 3;

            for (MNetPreset defaultPreset : presets)
            {
                menu.add(prepareWithPreset(defaultPreset, true), subIndex++);
            }
        }

        refreshDeviceWith(menu, device);

        return menu;
    }

    @Override
    public void refreshWith(ClimateTrayPreferences model)
    {
        refreshPresetsWith(model, model.isAnyDeviceSelected());
        refreshDevicesWith(model);

        MNetDevice activeDevice = model
            .getDevices()
            .stream()
            .filter(device -> (device.isEnabled()) && (device.isSelectedAndWorking()))
            .findFirst()
            .orElse(null);

        if (activeDevice != null)
        {
            String description = activeDevice.getState().describeAction();

            titlePanel
                .setDescription(ClimateTrayImage.ICON_THERMOMETER.getIcon(ClimateTrayImageState.NONE, 24), description);
        }
        else
        {
            titlePanel.setDescription(null, "Simple control utility for A/Cs");
        }

    }

    protected void refreshPresetsWith(ClimateTrayPreferences model, boolean enabled)
    {
        model.getPresets().forEach(preset -> {
            String id = preset.getId().toString();
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) dynamicItems.get(id);

            if (item == null)
            {
                return;
            }

            refreshPresetWith(item, preset, enabled);
        });
    }

    protected void refreshDevicesWith(ClimateTrayPreferences model)
    {
        model.getDevices().forEach(device -> {
            String id = device.getId().toString();
            JMenu menu = (JMenu) dynamicItems.get(id);

            if (menu != null)
            {
                refreshDeviceWith(menu, device);
            }
        });
    }

    protected void refreshPresetWith(JCheckBoxMenuItem item, MNetPreset preset, boolean enabled)
    {
        item.setText(preset.describe());
        item.setIcon(createIcon(preset));
        item.setSelected(preset.isSelected());
        item.setEnabled(enabled);
    }

    protected void refreshDeviceWith(JMenu menu, MNetDevice device)
    {
        menu.setText(device.describeStateAction());
        menu.setToolTipText(device.describeSettings());
        menu.setIcon(createIcon(device));
        //        menu.setSelected(device.isSelected());
        menu.setEnabled(device.isEnabled());

        MNetStatePanel titlePanel = (MNetStatePanel) dynamicItems.get(device.getId() + "#deviceTitle");

        if (titlePanel != null)
        {
            titlePanel.setDevice(device);
        }

        JCheckBoxMenuItem selectItem = (JCheckBoxMenuItem) dynamicItems.get(device.getId() + "#deviceSelect");

        if (selectItem != null)
        {
            refreshDeviceSelectWith(selectItem, device);
        }

        device.getPresets().forEach(preset -> {
            JCheckBoxMenuItem item = (JCheckBoxMenuItem) dynamicItems.get(String.valueOf(preset.getId()));

            if (item != null)
            {
                refreshDevicePresetWith(item, device, preset);
            }
        });
    }

    protected void refreshDeviceSelectWith(JCheckBoxMenuItem item, MNetDevice device)
    {
        item.setText("Controlled by Global Presets");
        item.setToolTipText("If this option is checked, the defined global presets will control this device.");
        item.setSelected(device.isSelected());
    }

    protected void refreshDevicePresetWith(JCheckBoxMenuItem item, MNetDevice device, MNetPreset preset)
    {
        item.setText(preset.describe());
        item.setIcon(createIcon(preset));
        item.setSelected(preset.isSelected());
    }

    protected Icon createIcon(MNetPreset preset)
    {
        ClimateTrayImageState imageState =
            (preset.isSelected()) ? ClimateTrayImageState.HIGHLIGHT : ClimateTrayImageState.NONE;

        return preset.createIcon(imageState, 16);
    }

    protected Icon createIcon(MNetDevice device)
    {
        ClimateTrayImageState imageState =
            (device.isSelected()) ? ClimateTrayImageState.HIGHLIGHT : ClimateTrayImageState.NONE;

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

    public void consume(Window owner)
    {
        Point location = MouseInfo.getPointerInfo().getLocation();

        consume(owner, location.x, location.y);
    }

    public void consume(Window owner, int x, int y)
    {
        JPopupMenu view = getView();

        if (view.isVisible())
        {
            view.setVisible(false);
        }

        monitor.block();

        try
        {
            prepareWith(PREFERENCES);
        }
        finally
        {
            monitor.unblock();
        }

        if (owner == null)
        {
            hiddenDialogForFocusManagement = new JDialog((Frame) null, "Climate Tray");

            hiddenDialogForFocusManagement.setUndecorated(true);
            hiddenDialogForFocusManagement
                .setIconImages(ClimateTrayImage.ICON.getImages(ClimateTrayImageState.NONE, 64, 48, 32, 24, 16));
            hiddenDialogForFocusManagement.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            hiddenDialogForFocusManagement.setVisible(true);

            owner = hiddenDialogForFocusManagement;
        }

        view.pack();
        view.show(owner, x, y);
    }

    public void presetSelect(MNetPreset preset)
    {
        ClimateTrayService.togglePreset(preset.getId());
    }

    public void deviceSelect(MNetDevice device)
    {
        ClimateTrayService.toggleDevice(device.getId());
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
