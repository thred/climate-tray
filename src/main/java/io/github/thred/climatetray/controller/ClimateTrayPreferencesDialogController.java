package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayPreferences;

public class ClimateTrayPreferencesDialogController extends
    AbstractClimateTrayDialogController<ClimateTrayPreferences, ClimateTrayPreferencesController>
{

    public ClimateTrayPreferencesDialogController()
    {
        super(new ClimateTrayPreferencesController());

        setTitle("Preferences");
    }

    @Override
    protected void success(ClimateTrayPreferences model)
    {
        ClimateTray.store();
    }

}
