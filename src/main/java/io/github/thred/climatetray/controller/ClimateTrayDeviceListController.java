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
package io.github.thred.climatetray.controller;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.github.thred.climatetray.controller.AbstractClimateTrayWindowController.Button;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.ui.MNetDeviceCellRenderer;
import io.github.thred.climatetray.util.MessageBuffer;

public class ClimateTrayDeviceListController extends AbstractClimateTrayListController<MNetDevice>
{

    public ClimateTrayDeviceListController()
    {
        super();

        list.setCellRenderer(new MNetDeviceCellRenderer());
    }

    @Override
    protected MNetDevice createElement()
    {
        return new MNetDevice();
    }

    @Override
    protected boolean consumeElement(MNetDevice device)
    {
        JPanel view = getView();
        
        return new ClimateTrayDeviceDialogController(SwingUtilities.windowForComponent(view)).consume(device) == Button.OK;
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        super.modified(messageBuffer);

        if (listModel.getSize() == 0)
        {
            messageBuffer.warn("Please, add at least one device you want to manage.");
        }
    }

}
