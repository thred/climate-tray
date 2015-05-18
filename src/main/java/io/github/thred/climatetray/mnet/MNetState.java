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

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.prefs.Prefs;

import javax.swing.Icon;

public class MNetState implements Copyable<MNetState>, Persistent
{

    private MNetMode mode = MNetMode.OFF;
    private Double temperature = Double.valueOf(22);
    private Double thermometer = null;
    private MNetFan fan = MNetFan.MID1;
    private MNetAir air = MNetAir.HORIZONTAL;

    public MNetState()
    {
        super();
    }

    public MNetState(MNetMode mode, Double temperature, Double thermometer, MNetFan fan, MNetAir air)
    {
        super();

        this.mode = mode;
        this.temperature = temperature;
        this.thermometer = thermometer;
        this.fan = fan;
        this.air = air;
    }

    @Override
    public MNetState deepCopy()
    {
        return new MNetState(mode, temperature, thermometer, fan, air);
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

    public Double getThermometer()
    {
        return thermometer;
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
        return MNetUtils.createIcon(state, size, mode, fan, thermometer, air);
    }

    public String describe(MNetStateType stateType)
    {
        StringBuilder builder = new StringBuilder(mode.getDescription());

        switch (stateType)
        {
            case NONE:
                break;

            case SETTING:
                if ((mode.isTemperatureEnabled()) && (temperature != null))
                {
                    builder.append(" ,").append(ClimateTray.PREFERENCES.getTemperatureUnit().format(temperature));
                }
                break;

            case STATE:
                if (thermometer != null)
                {
                    builder.append(" (").append(ClimateTray.PREFERENCES.getTemperatureUnit().format(thermometer))
                        .append(")");
                }
                break;

            case STATE_AND_SETTING:
                if (thermometer != null)
                {
                    builder.append(" (").append(ClimateTray.PREFERENCES.getTemperatureUnit().format(thermometer))
                        .append(")");
                }

                if ((mode.isTemperatureEnabled()) && (temperature != null))
                {
                    builder.append(", ").append(ClimateTray.PREFERENCES.getTemperatureUnit().format(temperature));
                }
                break;

            default:
                throw new UnsupportedOperationException("Type not supported: " + stateType);

        }

        if ((mode.isFanEnabled()) && (fan != null))
        {
            builder.append(", ").append(fan.getDescription());
        }

        if ((mode.isAirEnabled()) && (air != null))
        {
            builder.append(" (").append(air.getDescription()).append(")");
        }

        return builder.toString();
    }

    @Override
    public void read(Prefs prefs)
    {
        mode = prefs.getEnum(MNetMode.class, "mode", MNetMode.OFF);
        temperature = prefs.getDouble("temperature", temperature);
        thermometer = null;
        fan = prefs.getEnum(MNetFan.class, "fan", fan);
        air = prefs.getEnum(MNetAir.class, "air", air);
    }

    @Override
    public void write(Prefs prefs)
    {
        prefs.setEnum("mode", mode);
        prefs.setDouble("temperature", temperature);
        prefs.setEnum("fan", fan);
        prefs.setEnum("air", air);
    }

    @Override
    public String toString()
    {
        return "MNetState [mode=" + mode + ", temperature=" + temperature + ", thermometer=" + thermometer + ", fan="
            + fan + ", air=" + air + "]";
    }

}
