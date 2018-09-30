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
package io.github.thred.climatetray.mnet.ui;

import static io.github.thred.climatetray.ClimateTray.*;
import static io.github.thred.climatetray.util.swing.SwingUtils.*;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.mnet.MNetAdjust;
import io.github.thred.climatetray.mnet.MNetAir;
import io.github.thred.climatetray.mnet.MNetDrive;
import io.github.thred.climatetray.mnet.MNetFan;
import io.github.thred.climatetray.mnet.MNetMode;
import io.github.thred.climatetray.ui.AbstractClimateTrayController;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.ButtonPanel;
import io.github.thred.climatetray.util.swing.GBC;
import io.github.thred.climatetray.util.swing.SwingUtils;
import io.github.thred.climatetray.util.swing.ToggleIconGroup;

public class MNetAdjustController extends AbstractClimateTrayController<MNetAdjust, JComponent>
{

    private static final double MINIMUM = 17;
    private static final double MAXIMUM = 30;
    private static final int ICON_SIZE = 48;

    private final ToggleIconGroup<MNetDrive> driveButtons = monitor(new ToggleIconGroup<>());
    private final ToggleIconGroup<MNetMode> modeButtons = monitor(new ToggleIconGroup<>());
    private final ToggleIconGroup<MNetFan> fanButtons = monitor(new ToggleIconGroup<>());
    private final ToggleIconGroup<MNetAir> airButtons = monitor(new ToggleIconGroup<>());

    private double temperature;

    private final JButton coolerButton = SwingUtils
        .createIcon(ClimateTrayImage.ICON_LESS.getIcon(ClimateTrayImageState.NONE, ICON_SIZE), "Cooler", e -> cooler());
    private final JButton warmerButton = SwingUtils
        .createIcon(ClimateTrayImage.ICON_MORE.getIcon(ClimateTrayImageState.NONE, ICON_SIZE), "Warmer", e -> warmer());
    private final JLabel temperatureLabel = new JLabel("");

    public MNetAdjustController()
    {
        super();

        addMonitorListener(e -> update());
    }

    @Override
    protected JComponent createView()
    {
        JPanel view = new JPanel(new GridBagLayout());

        view.setOpaque(false);

        GBC gbc = new GBC(1, 5);

        ButtonPanel drivePanel = new ButtonPanel();

        for (MNetDrive drive : MNetDrive.values())
        {
            if (drive == MNetDrive.NO_CHANGE)
            {
                continue;
            }

            drivePanel
                .center(driveButtons
                    .put(drive, createToggleIcon(drive.getImage(), ICON_SIZE, drive.getLabel(),
                        e -> driveButtons.setValue(drive))));
        }

        view.add(drivePanel, gbc.fill());

        ButtonPanel modePanel = new ButtonPanel();

        for (MNetMode mode : MNetMode.values())
        {
            if (!mode.isSelectable() || mode == MNetMode.NO_CHANGE)
            {
                continue;
            }

            modePanel
                .center(modeButtons
                    .put(mode, createToggleIcon(mode.getImage(), ICON_SIZE, mode.getLabel(),
                        e -> modeButtons.setValue(mode))));
        }

        view.add(modePanel, gbc.next().fill());

        temperatureLabel.setFont(temperatureLabel.getFont().deriveFont(32f));
        temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
        temperatureLabel.setVerticalAlignment(SwingConstants.TOP);
        temperatureLabel.setPreferredSize(new Dimension(160, 64));

        ButtonPanel temperaturePanel = new ButtonPanel();

        temperaturePanel.center(coolerButton);
        temperaturePanel.center(temperatureLabel);
        temperaturePanel.center(warmerButton);

        view.add(temperaturePanel, gbc.next().fill());

        ButtonPanel fanPanel = new ButtonPanel();

        for (MNetFan fan : MNetFan.values())
        {
            if (fan == MNetFan.NO_CHANGE)
            {
                continue;
            }

            fanPanel
                .center(fanButtons
                    .put(fan,
                        createToggleIcon(fan.getImage(), ICON_SIZE, fan.getLabel(), e -> fanButtons.setValue(fan))));
        }

        view.add(fanPanel, gbc.next().fill());

        ButtonPanel airPanel = new ButtonPanel();

        for (MNetAir air : MNetAir.values())
        {
            if (air == MNetAir.NO_CHANGE)
            {
                continue;
            }

            airPanel
                .center(airButtons
                    .put(air,
                        createToggleIcon(air.getImage(), ICON_SIZE, air.getLabel(), e -> airButtons.setValue(air))));
        }

        view.add(airPanel, gbc.next().fill());

        return view;
    }

    @Override
    public void refreshWith(MNetAdjust model)
    {
        driveButtons.setValue(model.getDrive());
        modeButtons.setValue(model.getMode());
        temperature = model.getTemperature();
        fanButtons.setValue(model.getFan());
        airButtons.setValue(model.getAir());

        update();
    }

    private void cooler()
    {
        temperature = Math.max(temperature - 0.5, MINIMUM);
        update();
    }

    private void warmer()
    {
        temperature = Math.min(temperature + 0.5, MAXIMUM);
        update();
    }

    private void update()
    {
        temperatureLabel.setText(PREFERENCES.getTemperatureUnit().format(temperature));

        boolean enabled = driveButtons.getValue() != MNetDrive.OFF;

        modeButtons.setEnabled(enabled);
        fanButtons.setEnabled(enabled);
        airButtons.setEnabled(enabled);
        warmerButton.setEnabled(enabled && temperature < MAXIMUM);
        coolerButton.setEnabled(enabled && temperature > MINIMUM);
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        update();
    }

    @Override
    public void applyTo(MNetAdjust model)
    {
        model.setDrive(driveButtons.getValue());
        model.setMode(modeButtons.getValue());
        model.setTemperature(temperature);
        model.setFan(fanButtons.getValue());
        model.setAir(airButtons.getValue());
    }

    @Override
    public void dismiss(MNetAdjust model)
    {
        //        presetListController.dismiss(model.getPresets());
        //        deviceListController.dismiss(model.getDevices());
    }

}
