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

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetState;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MNetDeviceCellRenderer extends DefaultListCellRenderer
{

    private static final int ICON_SIZE = 24;

    private static final long serialVersionUID = 4726568871394455004L;

    public MNetDeviceCellRenderer()
    {
        super();
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
        boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value == null)
        {
            setText("");

            return this;
        }

        MNetDevice device = (MNetDevice) value;
        MNetState state = device.getState();
        ClimateTrayImageState imageState =
            (isSelected) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED;

        setIcon((state != null) ? state.createIcon(imageState, ICON_SIZE) : ClimateTrayImage.ICON.getIcon(imageState,
            ICON_SIZE));
        setText(device.describeSettings());
        setEnabled(device.isEnabled());
        
        return this;
    }
}
