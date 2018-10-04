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

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.ui.MNetPresetCellRenderer;

public class ClimateTrayPresetSelectController extends AbstractClimateTrayListSelectController<MNetPreset>
{

    public ClimateTrayPresetSelectController()
    {
        super();

        list.setCellRenderer(new MNetPresetCellRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    @Override
    protected String getTitle()
    {
        return "Presets:";
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = super.createView();

        upButton.setVisible(true);
        downButton.setVisible(true);
        removeButton.setVisible(true);

        return view;
    }

    @Override
    protected String describe(MNetPreset element)
    {
        return element.describe();
    }

}
