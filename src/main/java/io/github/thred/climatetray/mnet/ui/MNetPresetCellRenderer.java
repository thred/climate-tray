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
import io.github.thred.climatetray.mnet.MNetPreset;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MNetPresetCellRenderer extends DefaultListCellRenderer
{

    private static final long serialVersionUID = 8417832942032540610L;

    private static final int ICON_SIZE = 24;

    public MNetPresetCellRenderer()
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

        MNetPreset preset = (MNetPreset) value;
        ClimateTrayImageState imageState =
            (isSelected) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED;

        setIcon(preset.createIcon(imageState, ICON_SIZE));
        setText(preset.describe());

        return this;
    }

}
