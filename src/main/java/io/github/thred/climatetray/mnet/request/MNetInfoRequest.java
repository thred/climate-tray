package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.util.DomBuilder;

public class MNetInfoRequest extends AbstractMNetDeviceRequest
{

    public MNetInfoRequest()
    {
        super();
    }

    public MNetInfoRequest addDevice(MNetDevice device) throws MNetRequestException
    {
        if (device.getEc() == null)
        {
            throw new MNetRequestException("The EC is missing");
        }

        if (device.getAddress() == null)
        {
            throw new MNetRequestException("The address of the air conditioner is missing.");
        }

        addRequestItem(new MNetDeviceRequestItem(device));

        return this;
    }

    @Override
    protected String getRequestCommand()
    {
        return "getRequest";
    }

    @Override
    protected void buildRequestItemContent(DomBuilder builder, MNetDeviceRequestItem item) throws MNetRequestException
    {
        builder.attribute("Ec", item.getEc().getKey());
        builder.attribute("Address", item.getAddress());
        builder.attribute("Group", "*");
        builder.attribute("Model", "*");
    }

}
