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

import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.ui.MNetPresetCellRenderer;

public class ClimateTrayPresetSelectController extends AbstractClimateTraySelectController<MNetPreset>
{

    public ClimateTrayPresetSelectController()
    {
        super();

        list.setCellRenderer(new MNetPresetCellRenderer());

        //        list.addMouseListener(new MouseAdapter()
        //        {
        //            @Override
        //            public void mousePressed(MouseEvent e)
        //            {
        //                if (SwingUtilities.isRightMouseButton(e))
        //                {
        //                    Point point = e.getPoint();
        //
        //                    list.setSelectedIndex(list.locationToIndex(point));
        //
        //                    JPopupMenu menu = new JPopupMenu();
        //
        //                    menu.add(SwingUtils.createMenuItem("Delete", null, null, event -> {
        //                        int index = list.getSelectedIndex();
        //
        //                        if (index >= 0)
        //                        {
        //                            listModel.removeElementAt(index);
        //                        }
        //                    }));
        //
        //                    menu.show(list, point.x, point.y);
        //                }
        //            }
        //        });
    }

    @Override
    protected String describe(MNetPreset element)
    {
        return element.describe();
    }

}
