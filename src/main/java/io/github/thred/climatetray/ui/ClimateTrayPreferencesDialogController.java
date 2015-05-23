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

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.ClimateTrayService;

import java.awt.Window;

public class ClimateTrayPreferencesDialogController extends DefaultClimateTrayDialogController<ClimateTrayPreferences>
{

    public ClimateTrayPreferencesDialogController(Window owner)
    {
        super(owner, new ClimateTrayPreferencesController(), Button.OK, Button.CANCEL);

        setTitle("Preferences");
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
