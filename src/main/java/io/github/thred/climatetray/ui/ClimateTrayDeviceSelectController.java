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

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.ui.MNetDeviceCheckBoxCellRenderer;
import io.github.thred.climatetray.util.message.MessageBuffer;

public class ClimateTrayDeviceSelectController extends AbstractClimateTraySelectController<MNetDevice>
{

    public ClimateTrayDeviceSelectController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = super.createView();

        list.setCellRenderer(new MNetDeviceCheckBoxCellRenderer());
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //        list.setSelectionModel(new DefaultListSelectionModel()
        //        {
        //            private static final long serialVersionUID = 8964770425645736705L;
        //
        //            @Override
        //            public void addSelectionInterval(int index0, int index1)
        //            {
        //                if (super.isSelectedIndex(index0))
        //                {
        //                    super.removeSelectionInterval(index0, index1);
        //                }
        //                else
        //                {
        //                    super.addSelectionInterval(index0, index1);
        //                }
        //            }
        //        });

        return view;
    }

    @Override
    public void selected()
    {
        MNetDevice device = list.getSelectedValue();

        if (device == null)
        {
            return;
        }

        device.setSelected(!device.isSelected());
        list.clearSelection();
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        super.modified(messageBuffer);

        if (listModel.getSize() == 0)
        {
            messageBuffer.info("You can add global presets for managing all air conditioners at once.");
        }
    }

    @Override
    protected String describe(MNetDevice element)
    {
        return element.getName();
    }

}
