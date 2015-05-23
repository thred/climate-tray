package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDrive;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.DomBuilder;

import java.util.Locale;

public class MNetOperateRequest extends AbstractMNetDeviceRequest
{

    public MNetOperateRequest()
    {
        super();
    }

    @Override
    protected String getRequestCommand()
    {
        return "setRequest";
    }

    public MNetOperateRequest adjustDevice(MNetDevice device, MNetPreset preset) throws MNetRequestException
    {
        return addOperation(new MNetDeviceRequestItem(device, preset));
    }

    public MNetOperateRequest turnOff(MNetDevice device) throws MNetRequestException
    {
        MNetDeviceRequestItem item = new MNetDeviceRequestItem(device);

        item.setDrive(MNetDrive.OFF);

        return addOperation(item);
    }

    public MNetOperateRequest turnOn(MNetDevice device) throws MNetRequestException
    {
        MNetDeviceRequestItem item = new MNetDeviceRequestItem(device);

        item.setDrive(MNetDrive.ON);

        return addOperation(item);
    }

    protected MNetOperateRequest addOperation(MNetDeviceRequestItem item) throws MNetRequestException
    {
        if (item.getEc() == null)
        {
            throw new MNetRequestException("The EC is missing");
        }

        if (item.getGroup() == null)
        {
            throw new MNetRequestException("The group of the air conditioner is missing.");
        }

        if (item.getGroup() == null)
        {
            throw new MNetRequestException("The group of the air conditioner is missing.");
        }

        addRequestItem(item);

        return this;
    }

    @Override
    protected void buildRequestItemContent(DomBuilder builder, MNetDeviceRequestItem item) throws MNetRequestException
    {
        builder.attribute("Ec", item.getEc().getKey());
        builder.attribute("Group", item.getGroup());

        if (item.getDrive() != null)
        {
            builder.attribute("Drive", item.getDrive().getKey());
        }

        if (item.getMode() != null)
        {
            builder.attribute("Mode", item.getMode().getKey());
        }

        if (item.getTemperature() != null)
        {
            builder.attribute("SetTemp", String.format(Locale.US, "%.1f", item.getTemperature()));
        }

        if (item.getAir() != null)
        {
            builder.attribute("AirDirection", item.getAir().getKey());
        }

        if (item.getFan() != null)
        {
            builder.attribute("FanSpeed", item.getFan().getKey());
        }
    }
}
