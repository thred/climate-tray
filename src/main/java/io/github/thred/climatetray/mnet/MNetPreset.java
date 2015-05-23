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
package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.ClimateTray.*;
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
    private MNetFan fan = MNetFan.MID1;
    private MNetAir air = MNetAir.HORIZONTAL;
    private boolean selected = false;

    public MNetPreset()
    {
        super();
    }

    public MNetPreset(UUID id, MNetMode mode, Double temperature, MNetFan fan, MNetAir air, boolean selected)
    {
        super();

        this.id = id;
        this.mode = mode;
        this.temperature = temperature;
        this.fan = fan;
        this.air = air;
        this.selected = selected;
    }

    @Override
    public MNetPreset deepCopy()
    {
        return new MNetPreset(id, mode, temperature, fan, air, selected);
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

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
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
            builder.append(" (").append(PREFERENCES.getTemperatureUnit().format(temperature)).append(")");
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

        selected = false;
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

    @Override
    public String toString()
    {
        return "MNetPreset [id=" + id + ", mode=" + mode + ", temperature=" + temperature + ", fan=" + fan + ", air="
            + air + ", selected=" + selected + "]";
    }

}
