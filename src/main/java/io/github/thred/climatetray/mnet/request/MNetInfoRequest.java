package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.util.DomBuilder;

import java.util.Objects;

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

    public MNetDeviceRequestItem getItemByDeviceAddress(MNetDevice device)
    {
        return responseItems.stream().filter(item -> Objects.equals(device.getAddress(), item.getAddress()))
            .findFirst().orElse(null);
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
