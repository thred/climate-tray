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

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayPreferences;

public class ClimateTrayPreferencesDialogController extends
    AbstractClimateTrayDialogController<ClimateTrayPreferences, ClimateTrayPreferencesController>
{

    public ClimateTrayPreferencesDialogController()
    {
        super(new ClimateTrayPreferencesController(), BUTTON_OK_CANCEL);

        setTitle("Preferences");
    }

    @Override
    protected void success(ClimateTrayPreferences model)
    {
        ClimateTray.store();
    }

}
