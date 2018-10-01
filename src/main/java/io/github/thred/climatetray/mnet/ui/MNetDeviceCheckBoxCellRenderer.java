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
package io.github.thred.climatetray.mnet.ui;

import java.awt.Component;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetState;

public class MNetDeviceCheckBoxCellRenderer implements ListCellRenderer<MNetDevice>
{

    private static final int ICON_SIZE = 24;

    private final JCheckBoxMenuItem checkBox = new JCheckBoxMenuItem();

    public MNetDeviceCheckBoxCellRenderer()
    {
        super();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends MNetDevice> list, MNetDevice device, int index,
        boolean selected, boolean cellHasFocus)
    {
        MNetState state = device.getState();
        ClimateTrayImageState imageState =
            device.isSelected() ? ClimateTrayImageState.HIGHLIGHT : ClimateTrayImageState.DEFAULT;

        checkBox
            .setIcon((state != null) ? state.createIcon(imageState, ICON_SIZE)
                : ClimateTrayImage.ICON.getIcon(imageState, ICON_SIZE));
        checkBox.setText(device.describeState());
        checkBox.setSelected(device.isSelected());
        checkBox.setEnabled(device.isEnabled());

        return checkBox;
    }
}
