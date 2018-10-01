/*
 * Copyright 2015 - 2018 Manfred Hantschel
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

import java.util.Objects;

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
        builder.attribute("Drive", "*");
        builder.attribute("Mode", "*");
        builder.attribute("SetTemp", "*");
        builder.attribute("InletTemp", "*");
        builder.attribute("AirDirection", "*");
        builder.attribute("FanSpeed", "*");
    }

}
