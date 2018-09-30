/*
 * Copyright 2015, 2016 Manfred Hantschel
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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.MNetAdjust;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.ui.MNetPresetDialogController;
import io.github.thred.climatetray.util.swing.FooterPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class ClimateTrayAdjustAllDialogController extends DefaultClimateTrayDialogController<MNetAdjust>
{

    private final JButton storeButton = SwingUtils.createButton("Save as preset ...", e -> store());

    private final MNetAdjust adjust = new MNetAdjust();

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

            adjust.setPreset(items.get(0));
            refreshWith(adjust);
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
        applyTo(adjust);

        MNetPreset preset = new MNetPreset();

        preset.setDrive(adjust.getDrive());
        preset.setMode(adjust.getMode());
        preset.setTemperature(adjust.getTemperature());
        preset.setFan(adjust.getFan());
        preset.setAir(adjust.getAir());

        Button result = new MNetPresetDialogController(SwingUtilities.windowForComponent(getView())).consume(preset);

        if (result == Button.OK)
        {
            List<MNetPreset> presets = ClimateTray.PREFERENCES.getPresets();

            presets.add(preset);
            adjust.setPreset(preset);
            refreshWith(adjust);

            ClimateTrayService.store();
        }
    }

    @Override
    public void ok()
    {
        super.ok();

        applyTo(adjust);

        ClimateTrayService.toggleAdjust(adjust);
        ClimateTrayService.store();
        ClimateTrayService.scheduleUpdate();
    }

    @Override
    public void dismiss(MNetAdjust model)
    {
        LOG.debug("Closing adjust dialog.");

        super.dismiss(model);
    }

}
