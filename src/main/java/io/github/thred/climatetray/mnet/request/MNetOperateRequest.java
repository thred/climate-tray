/*
 * Copyright 2015, 2016 Manfred Hantschel
 *
 * This file is part of Climate-Tray.
 *
 * Climate-Tray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * Climate-Tray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Climate-Tray. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package io.github.thred.climatetray.mnet.request;

import java.util.Locale;
import java.util.Objects;

import io.github.thred.climatetray.mnet.MNetAdjust;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDrive;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.DomBuilder;

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

    public MNetOperateRequest adjustDevice(MNetDevice device, MNetAdjust adjust) throws MNetRequestException
    {
        return addOperation(new MNetDeviceRequestItem(device, adjust));
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

    public MNetDeviceRequestItem getItemByDeviceGroup(MNetDevice device)
    {
        return responseItems
            .stream()
            .filter(item -> Objects.equals(device.getGroup(), item.getGroup()))
            .findFirst()
            .orElse(null);
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
