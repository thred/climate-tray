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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ClimateTrayPreferencesController extends AbstractClimateTrayController<ClimateTrayPreferences, JComponent>
{

    private final ClimateTrayPresetListController presetListController = monitor(new ClimateTrayPresetListController());
    private final ClimateTrayDeviceListController deviceListController = monitor(new ClimateTrayDeviceListController());

    private final JSpinner updatePeriodInMunitesSpinner = monitor(createSpinner(new SpinnerNumberModel(1, 1, 360, 1)));
    private final JComboBox<TemperatureUnit> temperatureUnitBox = monitor(createComboBox(TemperatureUnit.values()));

    public ClimateTrayPreferencesController()
    {
        super();
    }

    @Override
    protected JComponent createView()
    {
        JPanel view = new JPanel(new GridBagLayout());

        updatePeriodInMunitesSpinner.setEditor(new JSpinner.NumberEditor(updatePeriodInMunitesSpinner, "0.0"));
        temperatureUnitBox.addActionListener((e) -> refresh());

        JPanel presetListView = presetListController.getView();
        JPanel deviceListView = deviceListController.getView();

        GBC gbc = new GBC(2, 6);

        view.add(createLabel("Update Period in Minutes:", updatePeriodInMunitesSpinner), gbc);
        view.add(updatePeriodInMunitesSpinner, gbc.next());

        view.add(createLabel("Unit of Temperature:", temperatureUnitBox), gbc.next());
        view.add(temperatureUnitBox, gbc.next());

        view.add(createLabel("Presets:", presetListView), gbc.next().top().insetTop(8));
        view.add(presetListView, gbc.next().weight(1, 1).fill());

        view.add(createLabel("Devices:", deviceListView), gbc.next().top().insetTop(8));
        view.add(deviceListView, gbc.next().weight(1, 1).fill());

        return view;
    }

    @Override
    public void prepareWith(ClimateTrayPreferences model)
    {
        updatePeriodInMunitesSpinner.setValue(model.getUpdatePeriodInMinutes());
        temperatureUnitBox.setSelectedItem(model.getTemperatureUnit());

        presetListController.prepareWith(model.getPresets());
        deviceListController.prepareWith(model.getDevices());
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        presetListController.modified(messageBuffer);
        deviceListController.modified(messageBuffer);
    }

    @Override
    public void applyTo(ClimateTrayPreferences model)
    {
        model.setUpdatePeriodInMinutes(((Number) updatePeriodInMunitesSpinner.getValue()).doubleValue());
        model.setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());

        presetListController.applyTo(model.getPresets());
        deviceListController.applyTo(model.getDevices());
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
