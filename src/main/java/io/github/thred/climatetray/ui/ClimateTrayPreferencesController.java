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
import static io.github.thred.climatetray.util.swing.SwingUtils.*;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.TemperatureUnit;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.GBC;

public class ClimateTrayPreferencesController extends AbstractClimateTrayController<ClimateTrayPreferences, JComponent>
{

    private final ClimateTrayDeviceListController deviceListController = monitor(new ClimateTrayDeviceListController());

    private final JSpinner updatePeriodInSecondsSpinner =
        monitor(createSpinner(new SpinnerNumberModel(60, 30, 60 * 60, 30)));
    private final JComboBox<TemperatureUnit> temperatureUnitBox = monitor(createComboBox(TemperatureUnit.values()));
    private final JCheckBox versionCheckEnabledBox = monitor(createCheckBox("Check for version updates after startup"));

    public ClimateTrayPreferencesController()
    {
        super();
    }

    @Override
    protected JComponent createView()
    {
        JPanel view = new JPanel(new GridBagLayout());

        view.setOpaque(false);

        updatePeriodInSecondsSpinner.setEditor(new JSpinner.NumberEditor(updatePeriodInSecondsSpinner, "0"));
        temperatureUnitBox.addActionListener((e) -> refresh());

        JPanel deviceListView = deviceListController.getView();

        GBC gbc = new GBC(2, 5);

        view.add(createLabel("Update Period in Seconds:", updatePeriodInSecondsSpinner), gbc);
        view.add(updatePeriodInSecondsSpinner, gbc.next());

        view.add(createLabel("Unit of Temperature:", temperatureUnitBox), gbc.next());
        view.add(temperatureUnitBox, gbc.next());

        view.add(createLabel("Version Updates:", versionCheckEnabledBox), gbc.next());
        view.add(versionCheckEnabledBox, gbc.next());

        view.add(createLabel("Air Conditioners:", deviceListView), gbc.next().top().insetTop(8));
        view.add(deviceListView, gbc.next().weight(1, 1).fill());

        return view;
    }

    @Override
    public void refreshWith(ClimateTrayPreferences model)
    {
        updatePeriodInSecondsSpinner.setValue(model.getUpdatePeriodInSeconds());
        temperatureUnitBox.setSelectedItem(model.getTemperatureUnit());
        versionCheckEnabledBox.setSelected(model.isVersionCheckEnabled());

        deviceListController.refreshWith(model.getDevices());
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        deviceListController.modified(messageBuffer);
    }

    @Override
    public void applyTo(ClimateTrayPreferences model)
    {
        model.setUpdatePeriodInSeconds(((Number) updatePeriodInSecondsSpinner.getValue()).intValue());
        model.setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());
        model.setVersionCheckEnabled(versionCheckEnabledBox.isSelected());

        deviceListController.applyTo(model.getDevices());
    }

    public void refresh()
    {
        PREFERENCES.setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());

        deviceListController.refresh();
    }

    @Override
    public void dismiss(ClimateTrayPreferences model)
    {
        deviceListController.dismiss(model.getDevices());
    }

    public void setVersionCheckEnabled(boolean enabled)
    {
        versionCheckEnabledBox.setSelected(enabled);
    }

}
