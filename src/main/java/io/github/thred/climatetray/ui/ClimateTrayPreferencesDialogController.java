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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.util.swing.FooterPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class ClimateTrayPreferencesDialogController extends DefaultClimateTrayDialogController<ClimateTrayPreferences>
{

    private final JButton proxyButton = SwingUtils.createButton("Proxy Settings", e -> proxySettings());
    private final ClimateTrayProxyDialogController proxyController =
        new ClimateTrayProxyDialogController(getView(), false);

    public ClimateTrayPreferencesDialogController(Window owner)
    {
        super(owner, new ClimateTrayPreferencesController(), Button.OK, Button.CANCEL);

        setTitle("Preferences");
    }

    @Override
    protected JComponent createBottomPanel(Button... buttons)
    {
        FooterPanel panel = (FooterPanel) super.createBottomPanel(buttons);

        panel.left(proxyButton);

        return panel;
    }

    public void setVersionCheckEnabled(boolean enabled)
    {
        ((ClimateTrayPreferencesController) controller).setVersionCheckEnabled(enabled);
    }

    public void proxySettings()
    {
        JDialog view = proxyController.getView();

        if (view.isVisible())
        {
            view.toFront();
        }
        else
        {
            proxyController.consume(getModel().getProxySettings());
        }
    }

    @Override
    public void ok()
    {
        super.ok();

        ClimateTrayService.store();
        ClimateTrayService.scheduleUpdate();
    }

    @Override
    public void dismiss(ClimateTrayPreferences model)
    {
        LOG.debug("Closing preferences dialog.");

        super.dismiss(model);
    }

}
