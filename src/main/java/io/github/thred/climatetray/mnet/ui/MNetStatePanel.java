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

import static io.github.thred.climatetray.util.swing.SwingUtils.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetState;
import io.github.thred.climatetray.util.swing.GBC;

public class MNetStatePanel extends JPanel
{

    private static final long serialVersionUID = 8760697714260150084L;

    private final JLabel nameLabel = new JLabel();
    private final JLabel therometerLabel = new JLabel();
    private final JLabel driveLabel = new JLabel();
    private final JLabel modeLabel = new JLabel();
    private final JLabel temperatureLabel = new JLabel();
    private final JLabel fanLabel = new JLabel();
    private final JLabel airLabel = new JLabel();

    public MNetStatePanel(MNetDevice device)
    {
        super(new GridBagLayout());

        nameLabel.setFont(nameLabel.getFont().deriveFont(nameLabel.getFont().getSize() * 2f));
        nameLabel.setForeground(new Color(0x191970));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        therometerLabel
            .setFont(therometerLabel.getFont().deriveFont(Font.BOLD, therometerLabel.getFont().getSize() * 1.2f));
        therometerLabel.setForeground(new Color(0x191970));

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        GBC gbc = new GBC(2, 7);

        add(nameLabel, gbc.span(2).hFill().left());

        //add(createLabel("Thermometer:"), gbc.next());
        add(therometerLabel, gbc.next().span(2));

        add(createLabel("Drive:"), gbc.next().weight(0.25));
        add(driveLabel, gbc.next().weight(0.75));

        add(createLabel("Mode:"), gbc.next());
        add(modeLabel, gbc.next());

        add(createLabel("Temperature:"), gbc.next());
        add(temperatureLabel, gbc.next());

        add(createLabel("Fan:"), gbc.next());
        add(fanLabel, gbc.next());

        add(createLabel("Air:"), gbc.next());
        add(airLabel, gbc.next());

        setDevice(device);
    }

    public void setDevice(MNetDevice device)
    {
        MNetState state = device.getState();
        Icon icon = state.createIcon(ClimateTrayImageState.DEFAULT, 24);

        nameLabel.setIcon(icon);
        nameLabel.setText(device.getName());

        if (state.getThermometer() != null)
        {
            therometerLabel.setText(state.describeAction());
        }
        else
        {
            therometerLabel.setText("");
        }

        if (state.getDrive() != null)
        {
            driveLabel.setIcon(state.getDrive().getImage().getIcon(ClimateTrayImageState.NONE, 16));
            driveLabel.setText(state.getDrive().getLabel());
        }
        else
        {
            driveLabel.setIcon(null);
            driveLabel.setText("");
        }

        if (state.getMode() != null)
        {
            modeLabel.setIcon(state.getMode().getImage().getIcon(ClimateTrayImageState.NONE, 16));
            modeLabel.setText(state.getMode().getLabel());
        }
        else
        {
            modeLabel.setIcon(null);
            modeLabel.setText("");
        }

        if (state.getTemperature() != null)
        {
            temperatureLabel.setIcon(ClimateTrayImage.ICON_THERMOMETER.getIcon(ClimateTrayImageState.NONE, 16));
            temperatureLabel.setText(ClimateTray.PREFERENCES.getTemperatureUnit().format(state.getTemperature()));
        }
        else
        {
            temperatureLabel.setText("");
        }

        if (state.getFan() != null)
        {
            fanLabel.setIcon(state.getFan().getImage().getIcon(ClimateTrayImageState.NONE, 16));
            fanLabel.setText(state.getFan().getLabel());
        }
        else
        {
            fanLabel.setIcon(null);
            fanLabel.setText("");
        }

        if (state.getAir() != null)
        {
            airLabel.setIcon(state.getAir().getImage().getIcon(ClimateTrayImageState.NONE, 16));
            airLabel.setText(state.getAir().getLabel());
        }
        else
        {
            airLabel.setIcon(null);
            airLabel.setText("");
        }
    }
}
