package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDeviceController;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.MessageList;

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

    @Override
    protected void localCheck(MessageList messages)
    {
        messages.addInfo("Look on the device for necessary informations.");

        super.localCheck(messages);
    }

    @Override
    protected void checked(boolean valid, MessageList messages)
    {
        super.checked(valid, messages);

        messages.sortBySeverity();
        if (messages.size() <= 0)
        {
            titlePanel.setDescription(null, null);

            return;
        }

        Message message = messages.get(0);

        titlePanel.setDescription(message.getSeverity().getImage().getIcon(ClimateTrayImageState.NONE, 16),
            message.getMessage());
    }

}
