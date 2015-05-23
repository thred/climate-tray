package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.util.DomBuilder;

public class MNetMonitorRequest extends AbstractMNetDeviceRequest
{

    public MNetMonitorRequest()
    {
        super();
    }

    @Override
    protected String getRequestCommand()
    {
        return "getRequest";
    }

    public MNetMonitorRequest addDevice(MNetDevice device) throws MNetRequestException
    {
        if (device.getEc() == null)
        {
            throw new MNetRequestException("The EC is missing");
        }

        if (device.getGroup() == null)
        {
            throw new MNetRequestException("The group of the air conditioner is missing.");
        }

        addRequestItem(new MNetDeviceRequestItem(device));

        return this;
    }

    @Override
    protected void buildRequestItemContent(DomBuilder builder, MNetDeviceRequestItem item) throws MNetRequestException
    {
        builder.attribute("Ec", item.getEc().getKey());
        builder.attribute("Group", item.getGroup());
        builder.attribute("Drive", "*");
        builder.attribute("Mode", "*");
        builder.attribute("SetTemp", "*");
        builder.attribute("InletTemp", "*");
        builder.attribute("AirDirection", "*");
        builder.attribute("FanSpeed", "*");
    }

}
