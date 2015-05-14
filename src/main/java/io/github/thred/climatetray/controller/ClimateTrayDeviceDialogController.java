package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDeviceController;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.Severity;

public class ClimateTrayDeviceDialogController extends
    AbstractClimateTrayDialogController<MNetDevice, MNetDeviceController>
{

    public ClimateTrayDeviceDialogController()
    {
        super(new MNetDeviceController());

        setTitle("Device");
        setDescription(new Message(Severity.INFO, "Look on the device for the necessary informations."));

        okButton.setEnabled(false);
    }

}
