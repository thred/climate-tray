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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Window;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.MNetAdjust;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.swing.FooterPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class ClimateTrayAdjustDialogController
    extends DefaultClimateTrayDialogController<MNetAdjust, ClimateTrayAdjustController>
{

    private final JButton storeButton = SwingUtils.createButton("Save as preset ...", e -> store());
    private final JButton preferencesButton = SwingUtils.createButton("Preferences ...", e -> preferences());

    public ClimateTrayAdjustDialogController(Window owner)
    {
        super(owner, new ClimateTrayAdjustController(), Button.OK, Button.CANCEL);

        setTitle("Adjust");

        controller.getPresetController().addSelectListener(e -> presetSelected());
    }

    @Override
    protected JComponent createBottomPanel(Button... buttons)
    {
        FooterPanel panel = (FooterPanel) super.createBottomPanel(buttons);

        panel.left(preferencesButton);
        panel.left(storeButton);

        return panel;
    }

    protected void presetSelected()
    {
        MNetPreset preset = controller.getPresetController().getSelectedValue();

        if (preset == null)
        {
            return;
        }

        MNetAdjust model = getModel();

        applyTo(model);

        model.getPreset().set(preset);

        refreshWith(model);
    }

    public void preferences()
    {
        cancel();
        ClimateTrayService.preferences();
    }

    public void store()
    {
        MNetAdjust model = getModel();

        applyTo(model);

        MNetPreset preset = model.getPreset();

        if (preset.isValid())
        {
            MNetPreset presetToStore = preset.deepCopy();

            presetToStore.setId(UUID.randomUUID());
            presetToStore.simplify();

            boolean empty = model.getPresets().isEmpty();
            boolean success = model.addPreset(presetToStore);

            if (empty && success)
            {
                SwingUtilities.invokeLater(() -> {
                    JDialog view = getView();
                    Dimension size = view.getSize();
                    Point location = view.getLocation();

                    view.pack();

                    Dimension newSize = view.getSize();

                    location.x = location.x + size.width - newSize.width;
                    location.y = location.y + size.height - newSize.height;

                    view.setLocation(location);
                });
            }
        }

        refreshWith(model);
    }

    @Override
    public void dismiss(MNetAdjust model)
    {
        LOG.debug("Closing adjust dialog.");

        super.dismiss(model);
    }

}
