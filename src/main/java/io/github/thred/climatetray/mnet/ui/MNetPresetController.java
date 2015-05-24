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
package io.github.thred.climatetray.mnet.ui;

import static io.github.thred.climatetray.ClimateTray.*;
import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.mnet.MNetAir;
import io.github.thred.climatetray.mnet.MNetDrive;
import io.github.thred.climatetray.mnet.MNetFan;
import io.github.thred.climatetray.mnet.MNetMode;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.ui.AbstractClimateTrayController;
import io.github.thred.climatetray.util.TemperatureUnit;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MNetPresetController extends AbstractClimateTrayController<MNetPreset, JPanel>
{

    private static final double DEFAULT = 22;
    private static final double MINIMUM = 17;
    private static final double MAXIMUM = 30;

    private final JComboBox<MNetDrive> driveBox = monitor(createComboBox(MNetDrive.values()));
    private final JComboBox<MNetMode> modeBox = monitor(createComboBox(MNetMode.selectableValues()));
    private final JCheckBox temperatureBox = monitor(createCheckBox(""));
    private final SpinnerNumberModel temperatureSpinnerModel = new SpinnerNumberModel(DEFAULT, MINIMUM, MAXIMUM, 0.5);
    private final JSpinner temperatureSpinner = monitor(createSpinner(temperatureSpinnerModel));
    private final JLabel temperatureLabel = createLabel("Temperature:", temperatureSpinner);
    private final JComboBox<MNetFan> fanBox = monitor(createComboBox(MNetFan.values()));
    private final JComboBox<MNetAir> airBox = monitor(createComboBox(MNetAir.values()));

    private JSpinner.NumberEditor temperatureSpinnerEditor = new JSpinner.NumberEditor(temperatureSpinner, "0.0");

    public MNetPresetController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new GridBagLayout());

        driveBox.setRenderer(new MNetDriveCellRenderer());
        modeBox.setRenderer(new MNetModeCellRenderer());

        temperatureSpinner.setEditor(temperatureSpinnerEditor);

        fanBox.setRenderer(new MNetFanCellRenderer());
        airBox.setRenderer(new MNetAirCellRenderer());

        GBC gbc = new GBC(3, 5);

        view.add(createLabel("Drive:", driveBox), gbc);
        view.add(driveBox, gbc.next().span(2).weight(1).hFill());

        view.add(createLabel("Mode:", modeBox), gbc.next());
        view.add(modeBox, gbc.next().span(2).weight(1).hFill());

        view.add(temperatureLabel, gbc.next());
        view.add(temperatureBox, gbc.next());
        view.add(temperatureSpinner, gbc.next().weight(1).hFill());

        view.add(createLabel("Fan:", fanBox), gbc.next());
        view.add(fanBox, gbc.next().span(2).weight(1).hFill());

        view.add(createLabel("Air:", airBox), gbc.next());
        view.add(airBox, gbc.next().span(2).weight(1).hFill());

        return view;
    }

    @Override
    public void refreshWith(MNetPreset model)
    {
        driveBox.setSelectedItem(Utils.ensure(model.getDrive(), MNetDrive.NO_CHANGE));
        modeBox.setSelectedItem(Utils.ensure(model.getMode(), MNetMode.NO_CHANGE));

        TemperatureUnit temperatureUnit = PREFERENCES.getTemperatureUnit();

        temperatureLabel.setText(String.format("Temperature (in %s):", temperatureUnit.getSymbol()));

        Double temperature = model.getTemperature();

        temperatureBox.setSelected(temperature != null);

        temperatureSpinnerModel.setMinimum(temperatureUnit.convertFromCelsius(MINIMUM));
        temperatureSpinnerModel.setMaximum(temperatureUnit.convertFromCelsius(MAXIMUM));
        temperatureSpinnerModel.setStepSize(temperatureUnit.getStep());

        temperatureSpinnerEditor = new JSpinner.NumberEditor(temperatureSpinner, temperatureUnit.getNumberFormat());

        temperatureSpinner.setEditor(temperatureSpinnerEditor);
        temperatureSpinner.setValue(temperatureUnit.convertFromCelsius(Utils.ensure(temperature, 22.0)));

        fanBox.setSelectedItem(Utils.ensure(model.getFan(), MNetFan.NO_CHANGE));
        airBox.setSelectedItem(Utils.ensure(model.getAir(), MNetAir.NO_CHANGE));
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        boolean any = false;

        MNetDrive drive = (MNetDrive) driveBox.getSelectedItem();

        any |= drive != MNetDrive.NO_CHANGE;

        modeBox.setEnabled(drive != MNetDrive.OFF);

        MNetMode mode = (MNetMode) modeBox.getSelectedItem();

        any |= mode != MNetMode.NO_CHANGE;

        temperatureBox.setEnabled((drive != MNetDrive.OFF) && (mode.isTemperatureEnabled()));

        boolean temperatureEnabled = temperatureBox.isSelected();

        any |= temperatureEnabled;

        temperatureSpinner
            .setEnabled((drive != MNetDrive.OFF) && (mode.isTemperatureEnabled()) && (temperatureEnabled));

        any |= ((MNetFan) fanBox.getSelectedItem()) != MNetFan.NO_CHANGE;

        fanBox.setEnabled((drive != MNetDrive.OFF) && (mode.isFanEnabled()));

        any |= ((MNetAir) airBox.getSelectedItem()) != MNetAir.NO_CHANGE;

        airBox.setEnabled((drive != MNetDrive.OFF) && (mode.isAirEnabled()));

        if (!any)
        {
            messageBuffer.error("Select at least one option.");
        }
    }

    @Override
    public void applyTo(MNetPreset model)
    {
        MNetDrive drive = (MNetDrive) driveBox.getSelectedItem();

        model.setDrive(drive);

        MNetMode mode = (drive == MNetDrive.OFF) ? MNetMode.NO_CHANGE : (MNetMode) modeBox.getSelectedItem();

        model.setMode(mode);

        TemperatureUnit temperatureUnit = PREFERENCES.getTemperatureUnit();
        Double temperature =
            ((drive == MNetDrive.OFF) || (!mode.isTemperatureEnabled()) || (!temperatureBox.isSelected())) ? null
                : temperatureUnit.convertToCelsius((Double) temperatureSpinner.getValue());

        model.setTemperature(temperature);

        model.setFan((drive == MNetDrive.OFF) || (!mode.isFanEnabled()) ? null : (MNetFan) fanBox.getSelectedItem());
        model.setAir((drive == MNetDrive.OFF) || (!mode.isAirEnabled()) ? null : (MNetAir) airBox.getSelectedItem());
    }

    @Override
    public void dismiss(MNetPreset model)
    {
        // intentionally left blank
    }
}
