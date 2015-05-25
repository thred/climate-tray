/*
 * Copyright 2015 Manfred Hantschel
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

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.mnet.MNetAir;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDrive;
import io.github.thred.climatetray.mnet.MNetEc;
import io.github.thred.climatetray.mnet.MNetFan;
import io.github.thred.climatetray.mnet.MNetMode;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.DomUtils;

import org.w3c.dom.Node;

public class MNetDeviceRequestItem
{
    public static MNetDeviceRequestItem parse(Node node)
    {
        MNetEc ec = MNetEc.valueOfKey(DomUtils.getAttribute(node, "Ec"));
        Integer address = valueOfIntegerKey(DomUtils.getAttribute(node, "Address"));
        Integer group = valueOfIntegerKey(DomUtils.getAttribute(node, "Group"));
        MNetDeviceRequestItem result = new MNetDeviceRequestItem(ec, address, group);

        result.setModel(valueOfStringKey(DomUtils.getAttribute(node, "Model")));
        result.setDrive(MNetDrive.valueOfKey(DomUtils.getAttribute(node, "Drive")));
        result.setMode(MNetMode.valueOfKey(DomUtils.getAttribute(node, "Mode")));
        result.setTemperature(valueOfDoubleKey(DomUtils.getAttribute(node, "SetTemp")));
        result.setThermometer(valueOfDoubleKey(DomUtils.getAttribute(node, "InletTemp")));
        result.setAir(MNetAir.valueOfKey(DomUtils.getAttribute(node, "AirDirection")));
        result.setFan(MNetFan.valueOfKey(DomUtils.getAttribute(node, "FanSpeed")));

        return result;
    }

    private final MNetEc ec;
    private final Integer address;
    private final Integer group;

    private String model;
    private MNetDrive drive;
    private MNetMode mode;
    private Double temperature;
    private Double thermometer;
    private MNetAir air;
    private MNetFan fan;

    public MNetDeviceRequestItem(MNetDevice device)
    {
        this(device.getEc(), device.getAddress(), device.getGroup());
    }

    public MNetDeviceRequestItem(MNetDevice device, MNetPreset preset)
    {
        this(device);

        MNetDrive drive = preset.getDrive();

        if (drive != MNetDrive.NO_CHANGE)
        {
            setDrive(drive);
        }

        MNetMode mode = preset.getMode();

        if (mode != MNetMode.NO_CHANGE)
        {
            setMode(mode);
        }

        Double temperature = preset.getTemperature();

        if (temperature != null)
        {
            setTemperature(temperature);
        }

        MNetFan fan = preset.getFan();

        if (fan != MNetFan.NO_CHANGE)
        {
            setFan(fan);
        }

        MNetAir air = preset.getAir();

        if (air != MNetAir.NO_CHANGE)
        {
            setAir(device.getInstallation().translate(air));
        }
    }

    public MNetDeviceRequestItem(MNetEc ec, Integer address, Integer group)
    {
        super();

        this.ec = ec;
        this.address = address;
        this.group = group;
    }

    public MNetEc getEc()
    {
        return ec;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public MNetDrive getDrive()
    {
        return drive;
    }

    public void setDrive(MNetDrive drive)
    {
        this.drive = drive;
    }

    public MNetMode getMode()
    {
        return mode;
    }

    public void setMode(MNetMode mode)
    {
        this.mode = mode;
    }

    public Integer getAddress()
    {
        return address;
    }

    public Integer getGroup()
    {
        return group;
    }

    public Double getTemperature()
    {
        return temperature;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    public Double getThermometer()
    {
        return thermometer;
    }

    public void setThermometer(Double thermometer)
    {
        this.thermometer = thermometer;
    }

    public MNetAir getAir()
    {
        return air;
    }

    public void setAir(MNetAir air)
    {
        this.air = air;
    }

    public MNetFan getFan()
    {
        return fan;
    }

    public void setFan(MNetFan fan)
    {
        this.fan = fan;
    }

    public void update(MNetDevice device)
    {
        if (ec != null)
        {
            device.setEc(ec);
        }

        if (address != null)
        {
            device.setAddress(address);
        }

        if ((group != null) && (!Integer.valueOf(99).equals(group)))
        {
            device.setGroup(group);
        }

        if ((model != null) && (!"*".equals(model)))
        {
            device.setModel(model);
        }

        if (drive != null)
        {
            device.getState().setDrive(drive);
        }

        if (mode != null)
        {
            device.getState().setMode(mode);
        }

        if (temperature != null)
        {
            device.getState().setTemperature(temperature);
        }

        if (thermometer != null)
        {
            device.getState().setThermometer(thermometer);
        }

        if (air != null)
        {
            device.getState().setAir(device.getInstallation().translate(air));
        }

        if (fan != null)
        {
            device.getState().setFan(fan);
        }
    }

    @Override
    public String toString()
    {
        return "MNetDeviceRequestItem [ec=" + ec + ", address=" + address + ", group=" + group + ", model=" + model
            + ", drive=" + drive + ", mode=" + mode + ", temperature=" + temperature + ", thermometer=" + thermometer
            + ", air=" + air + ", fan=" + fan + "]";
    }

    protected static String valueOfStringKey(String value)
    {
        if ((value == null) || ("*".equals(value)))
        {
            return null;
        }

        return value;
    }

    protected static Integer valueOfIntegerKey(String value)
    {
        if ((value == null) || ("*".equals(value)))
        {
            return null;
        }

        try
        {
            return Integer.valueOf(value);
        }
        catch (NumberFormatException e)
        {
            LOG.error("Failed to parse number: %s", e, value);
        }

        return null;
    }

    protected static Double valueOfDoubleKey(String value)
    {
        if ((value == null) || ("*".equals(value)))
        {
            return null;
        }

        try
        {
            return Double.valueOf(value);
        }
        catch (NumberFormatException e)
        {
            LOG.error("Failed to parse number: %s", e, value);
        }

        return null;
    }

}
