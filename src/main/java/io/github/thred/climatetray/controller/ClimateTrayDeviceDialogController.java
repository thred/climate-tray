package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDeviceController;

public class ClimateTrayDeviceDialogController extends
    AbstractClimateTrayDialogController<MNetDevice, MNetDeviceController>
{

    public ClimateTrayDeviceDialogController()
    {
        super(new MNetDeviceController());
    }

    @Override
    protected String getTitle()
    {
        return "Device";
    }

}
