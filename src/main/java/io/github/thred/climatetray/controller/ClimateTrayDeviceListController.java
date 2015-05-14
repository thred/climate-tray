package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDeviceCellRenderer;
import io.github.thred.climatetray.util.MessageList;

public class ClimateTrayDeviceListController extends AbstractClimateTrayListController<MNetDevice>
{

    public ClimateTrayDeviceListController()
    {
        super();

        list.setCellRenderer(new MNetDeviceCellRenderer());
    }

    @Override
    protected MNetDevice createElement()
    {
        return new MNetDevice();
    }

    @Override
    protected boolean consumeElement(MNetDevice device)
    {
        return new ClimateTrayDeviceDialogController().consume(getView(), device);
    }

    @Override
    public void modified(MessageList messages)
    {
        super.modified(messages);

        if (listModel.getSize() == 0)
        {
            messages.addWarning("Please, add at least one device you want to manage.");
        }
    }

}
