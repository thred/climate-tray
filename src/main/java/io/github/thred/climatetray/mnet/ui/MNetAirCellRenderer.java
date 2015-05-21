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

import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.mnet.MNetAir;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MNetAirCellRenderer extends DefaultListCellRenderer
{

    private static final long serialVersionUID = -1286708018290981191L;

    public MNetAirCellRenderer()
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

        MNetAir air = (MNetAir) value;

        setIcon(air.getImage().getIcon((isSelected) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED, 16));
        setText(air.getLabel());

        return this;
    }
}
