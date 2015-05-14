package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.prefs.Prefs;

import javax.swing.Icon;

public class MNetState implements Copyable<MNetState>, Persistent
{

    private MNetMode mode;
    private Double temperature;
    private MNetFan fan;
    private MNetAir air;

    public MNetState()
    {
        super();
    }

    public MNetState(MNetMode mode, Double temperature, MNetFan fan, MNetAir air)
    {
        super();

        this.mode = mode;
        this.temperature = temperature;
        this.fan = fan;
        this.air = air;
    }

    @Override
    public MNetState deepCopy()
    {
        return new MNetState(mode, temperature, fan, air);
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
