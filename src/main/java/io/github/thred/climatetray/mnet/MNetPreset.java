package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.prefs.Prefs;

import javax.swing.Icon;

public class MNetPreset implements Copyable<MNetPreset>, Persistent
{

    private MNetMode mode = MNetMode.OFF;
    private Double temperature = Double.valueOf(22);
    private MNetFan fan = MNetFan.MEDIUM_1;
    private MNetAir air = MNetAir.POSITION_1;

    public MNetPreset()
    {
        super();
    }

    public MNetPreset(MNetMode mode, Double temperature, MNetFan fan, MNetAir air)
    {
        super();

        this.mode = mode;
        this.temperature = temperature;
        this.fan = fan;
        this.air = air;
    }

    @Override
    public MNetPreset deepCopy()
    {
        return new MNetPreset(mode, temperature, fan, air);
    }

    public MNetMode getMode()
    {
        return mode;
    }

    public void setMode(MNetMode mode)
    {
        this.mode = mode;
    }

    public Double getTemperature()
    {
        return temperature;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    public MNetFan getFan()
    {
        return fan;
    }

    public void setFan(MNetFan fan)
    {
        this.fan = fan;
    }

    public MNetAir getAir()
    {
        return air;
    }

    public void setAir(MNetAir air)
    {
        this.air = air;
    }

    public Icon createIcon(ClimateTrayImageState state, int size)
    {
        return MNetUtils.createIcon(state, size, mode, fan, temperature, air);
    }

    public String describe()
    {
        StringBuilder builder = new StringBuilder(mode.getLabel());

        if (mode.isTemperatureEnabled())
        {
            builder.append(String.format(" %.1f°C", temperature));
        }

        if (mode.isFanEnabled())
        {
            builder.append(" ").append(fan.getLabel());
        }

        if (mode.isAirEnabled())
        {
            builder.append(" ").append(air.getLabel());
        }

        return builder.toString();
    }

    @Override
    public void read(Prefs prefs)
    {
        mode = prefs.getEnum(MNetMode.class, "mode", MNetMode.OFF);
        temperature = prefs.getDouble("temperature", null);
        fan = prefs.getEnum(MNetFan.class, "fan", null);
        air = prefs.getEnum(MNetAir.class, "air", null);
    }

    @Override
    public void write(Prefs prefs)
    {
        prefs.setEnum("mode", mode);
        prefs.setDouble("temperature", temperature);
        prefs.setEnum("fan", fan);
        prefs.setEnum("air", air);
    }

}
