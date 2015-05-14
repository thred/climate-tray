package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.MNetPresetController;

public class ClimateTrayPresetDialogController extends
    AbstractClimateTrayDialogController<MNetPreset, MNetPresetController>
{

    public ClimateTrayPresetDialogController()
    {
        super(new MNetPresetController());

        setTitle("Preset");
    }

}
