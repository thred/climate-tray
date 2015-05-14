package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.prefs.Prefs;

import java.util.UUID;

import javax.swing.Icon;

public class MNetPreset implements Copyable<MNetPreset>, Persistent
{

    private UUID id = UUID.randomUUID();
    private MNetMode mode = MNetMode.OFF;
    private Double temperature = Double.valueOf(22);
    private MNetFan fan = MNetFan.MEDIUM_1;
    private MNetAir air = MNetAir.POSITION_1;

    public MNetPreset()
    {
        super();
    }

    public MNetPreset(UUID id, MNetMode mode, Double temperature, MNetFan fan, MNetAir air)
    {
        super();

        this.id = id;
        this.mode = mode;
        this.temperature = temperature;
        this.fan = fan;
        this.air = air;
    }

    @Override
    public MNetPreset deepCopy()
    {
        return new MNetPreset(id, mode, temperature, fan, air);
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
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
        StringBuilder builder = new StringBuilder(mode.getDescription());

        if (mode.isTemperatureEnabled())
        {
            builder.append(" (").append(ClimateTray.PREFERENCES.getTemperatureUnit().format(temperature)).append(")");
        }

        if (mode.isFanEnabled())
        {
            builder.append(", ").append(fan.getDescription());
        }

        if (mode.isAirEnabled())
        {
            builder.append(" (").append(air.getDescription()).append(")");
        }

        return builder.toString();
    }

    @Override
    public void read(Prefs prefs)
    {
        id = prefs.getUUID("id", id);
        mode = prefs.getEnum(MNetMode.class, "mode", MNetMode.OFF);
        temperature = prefs.getDouble("temperature", temperature);
        fan = prefs.getEnum(MNetFan.class, "fan", fan);
        air = prefs.getEnum(MNetAir.class, "air", air);
    }

    @Override
    public void write(Prefs prefs)
    {
        prefs.setUUID("id", id);
        prefs.setEnum("mode", mode);
        prefs.setDouble("temperature", temperature);
        prefs.setEnum("fan", fan);
        prefs.setEnum("air", air);
    }

}
