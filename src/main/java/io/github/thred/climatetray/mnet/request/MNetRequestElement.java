package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.mnet.MNetAir;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetEc;
import io.github.thred.climatetray.mnet.MNetFan;
import io.github.thred.climatetray.mnet.MNetMode;
import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;

import java.util.Locale;

import org.w3c.dom.Node;

public class MNetRequestElement
{
    public static MNetRequestElement parse(Node node)
    {
        Integer group = DomUtils.getIntegerAttribute(node, "Group", null);
        String driveString = DomUtils.getAttribute(node, "Drive", "OFF");
        String modeString = DomUtils.getAttribute(node, "Mode", null);
        Integer ec = DomUtils.getIntegerAttribute(node, "Ec", null);
        Double temperature = DomUtils.getDoubleAttribute(node, "SetTemp", null);
        Double thermometer = DomUtils.getDoubleAttribute(node, "InletTemp", null);
        String airString = DomUtils.getAttribute(node, "AirDirection", null);
        String fanString = DomUtils.getAttribute(node, "FanSpeed", null);

        if ("OFF".equals(driveString))
        {
            modeString = "OFF";
        }

        if (("AUTOHEAT".equals(modeString)) || ("AUTOCOOL".equals(modeString)))
        {
            modeString = "AUTO";
        }

        MNetMode mode = null;

        if (modeString != null)
        {
            try
            {
                mode = MNetMode.valueOf(modeString);
            }
            catch (IllegalArgumentException e)
            {
                ClimateTray.LOG.error("Failed to parse mode %s", e, modeString);
            }
        }

        MNetRequestElement result = new MNetRequestElement(null, group, mode);

        // TODO fix ec
        //        if (ec != null)
        //        {
        //            result.setEc(ec);
        //        }

        if (temperature != null)
        {
            result.setTemperature(temperature);
        }

        if (thermometer != null)
        {
            result.setThermometer(thermometer);
        }

        if (airString != null)
        {
            try
            {
                result.setAir(MNetAir.valueOf(airString));
            }
            catch (IllegalArgumentException e)
            {
                ClimateTray.LOG.error("Failed to parse air %s", e, airString);
            }
        }

        if (fanString != null)
        {
            try
            {
                result.setFan(MNetFan.valueOf(fanString));
            }
            catch (IllegalArgumentException e)
            {
                ClimateTray.LOG.error("Failed to parse fan %s", e, fanString);
            }
        }

        return result;
    }

    private final Integer group;
    private final MNetMode mode;

    private MNetEc ec;
    private Double temperature;
    private Double thermometer;
    private MNetAir air;
    private MNetFan fan;

    public MNetRequestElement(MNetDevice device)
    {
        this(device, null);
    }

    public MNetRequestElement(MNetDevice device, MNetMode mode)
    {
        this(device.getEc(), device.getGroup(), mode);
    }

    public MNetRequestElement(MNetEc ec, Integer group, MNetMode mode)
    {
        super();

        this.ec = ec;
        this.group = group;
        this.mode = mode;
    }

    public Integer getGroup()
    {
        return group;
    }

    public MNetMode getMode()
    {
        return mode;
    }

    public MNetEc getEc()
    {
        return ec;
    }

    public void setEc(MNetEc ec)
    {
        this.ec = ec;
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

    public void buildOperateRequest(DomBuilder builder)
    {
        builder.begin("Mnet");

        if (ec != null)
        {
            builder.attribute("Ec", ec.getKey());
        }

        builder.attribute("Group", group);
        builder.attribute("Drive", mode.getDriveKey());
        builder.attribute("Mode", mode.getKey());

        if (temperature != null)
        {
            builder.attribute("SetTemp", String.format(Locale.US, "%.1f", temperature));
        }

        if (air != null)
        {
            builder.attribute("AirDirection", air.getKey());
        }

        if (fan != null)
        {
            builder.attribute("FanSpeed", fan.getKey());
        }

        builder.end();
    }

    public void buildMonitorRequest(DomBuilder builder)
    {
        builder.begin("Mnet");

        if (ec != null)
        {
            builder.attribute("Ec", ec.getKey());
        }

        builder.attribute("Group", group);
        builder.attribute("Drive", "*");
        builder.attribute("Mode", "*");
        builder.attribute("SetTemp", "*");
        builder.attribute("AirDirection", "*");
        builder.attribute("FanSpeed", "*");

        builder.end();
    }

    @Override
    public String toString()
    {
        return "MNetRequestElement [group=" + group + ", mode=" + mode + ", ec=" + ec + ", temperature=" + temperature
            + ", thermometer=" + thermometer + ", air=" + air + ", fan=" + fan + "]";
    }

}
