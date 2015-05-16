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
package io.github.thred.climatetray.controller;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.TemperatureUnit;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ClimateTrayPreferencesController extends AbstractClimateTrayController<ClimateTrayPreferences, JComponent>
{

    private final ClimateTrayPresetListController presetListController = monitor(new ClimateTrayPresetListController());
    private final ClimateTrayDeviceListController deviceListController = monitor(new ClimateTrayDeviceListController());

    private final JComboBox<TemperatureUnit> temperatureUnitBox = monitor(createComboBox(TemperatureUnit.values()));

    private final JPanel view = new JPanel(new GridBagLayout());

    public ClimateTrayPreferencesController()
    {
        super();

        temperatureUnitBox.addActionListener((e) -> refresh());

        JPanel presetListView = presetListController.getView();
        JPanel deviceListView = deviceListController.getView();

        GBC gbc = new GBC(2, 6);

        view.add(createLabel("Unit of Temperature:", temperatureUnitBox), gbc);
        view.add(temperatureUnitBox, gbc.next());

        view.add(createLabel("Presets:", presetListView), gbc.next().top().insetTop(8));
        view.add(presetListView, gbc.next().weight(1, 1).fill());

        view.add(createLabel("Devices:", deviceListView), gbc.next().top().insetTop(8));
        view.add(deviceListView, gbc.next().weight(1, 1).fill());
    }

    @Override
    public JComponent getView()
    {
        return view;
    }

    @Override
    public void prepare(ClimateTrayPreferences model)
    {
        temperatureUnitBox.setSelectedItem(model.getTemperatureUnit());

        presetListController.prepare(model.getPresets());
        deviceListController.prepare(model.getDevices());
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        presetListController.modified(messageBuffer);
        deviceListController.modified(messageBuffer);
    }

    @Override
    public void apply(ClimateTrayPreferences model)
    {
        model.setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());

        presetListController.apply(model.getPresets());
        deviceListController.apply(model.getDevices());
    }

    public void refresh()
    {
        ClimateTray.PREFERENCES.setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());

        presetListController.refresh();
        deviceListController.refresh();
    }

    @Override
    public void dismiss(ClimateTrayPreferences model)
    {
        presetListController.dismiss(model.getPresets());
        deviceListController.dismiss(model.getDevices());
    }

}
