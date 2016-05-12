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
package io.github.thred.climatetray.ui;

import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.ui.MNetPresetCellRenderer;
import io.github.thred.climatetray.mnet.ui.MNetPresetDialogController;
import io.github.thred.climatetray.ui.AbstractClimateTrayWindowController.Button;
import io.github.thred.climatetray.util.message.MessageBuffer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ClimateTrayPresetListController extends AbstractClimateTrayListController<MNetPreset>
{

    public ClimateTrayPresetListController()
    {
        super();

        list.setCellRenderer(new MNetPresetCellRenderer());
    }

    @Override
    protected MNetPreset createElement()
    {
        return new MNetPreset();
    }

    @Override
    protected boolean consumeElement(MNetPreset preset)
    {
        JPanel view = getView();

        return new MNetPresetDialogController(SwingUtilities.windowForComponent(view)).consume(preset) == Button.OK;
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
    protected String describe(MNetPreset element)
    {
        return element.describe();
    }

}
