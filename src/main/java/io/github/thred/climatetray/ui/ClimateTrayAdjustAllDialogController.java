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

import static io.github.thred.climatetray.ClimateTray.*;

import java.awt.Window;
import java.util.List;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComponent;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.swing.FooterPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class ClimateTrayAdjustAllDialogController extends DefaultClimateTrayDialogController<MNetPreset>
{

    private final JButton storeButton = SwingUtils.createButton("Save as preset ...", e -> store());

    private final MNetPreset preset = new MNetPreset();

    public ClimateTrayAdjustAllDialogController(Window owner)
    {
        super(owner, new ClimateTrayAdjustAllController(), Button.OK, Button.CANCEL);

        setTitle("Adjust");

        ((ClimateTrayAdjustAllController) controller).addSelectListener(e -> {
            List<MNetPreset> items = e.getItems();

            if (items == null || items.size() < 1)
            {
                return;
            }

            preset.set(items.get(0));
            refreshWith(preset);
        });
    }

    @Override
    protected JComponent createBottomPanel(Button... buttons)
    {
        FooterPanel panel = (FooterPanel) super.createBottomPanel(buttons);

        panel.left(storeButton);

        return panel;
    }

    public void store()
    {
        applyTo(preset);

        if (preset.isValid())
        {
            MNetPreset presetToStore = preset.deepCopy();

            presetToStore.setId(UUID.randomUUID());

            List<MNetPreset> presets = ClimateTray.PREFERENCES.getPresets();

            presets.add(presetToStore);

            ClimateTrayService.store();
        }

        refreshWith(preset);
    }

    @Override
    public void ok()
    {
        super.ok();

        applyTo(preset);

        ClimateTrayService.togglePreset(preset);
        ClimateTrayService.store();
        ClimateTrayService.scheduleUpdate();
    }

    @Override
    public void dismiss(MNetPreset model)
    {
        LOG.debug("Closing adjust dialog.");

        super.dismiss(model);
    }

}
