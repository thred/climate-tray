package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDeviceController;

public class ClimateTrayDeviceController extends AbstractClimateTrayDialogController<MNetDevice, MNetDeviceController>
{

    public ClimateTrayDeviceController()
    {
        super(new MNetDeviceController());
    }

    @Override
    protected String getTitle()
    {
        return "Device";
    }

    @Override
    protected void localPrepare(MNetDevice model)
    {
        super.localPrepare(model);

        getView().setTitle(model.getType().getName());
    }
}
