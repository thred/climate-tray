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
package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.util.Copyable;

public class MNetAdjust implements Copyable<MNetAdjust>
{

    private MNetDrive drive = MNetDrive.OFF;
    private MNetMode mode = MNetMode.AUTO;
    private Double temperature = 22d;
    private MNetFan fan = MNetFan.MID1;
    private MNetAir air = MNetAir.SWING;

    public MNetAdjust()
    {
        super();
    }

    public MNetAdjust(MNetDrive drive, MNetMode mode, Double temperature, MNetFan fan, MNetAir air)
    {
        super();

        this.drive = drive;
        this.mode = mode;
        this.temperature = temperature;
        this.fan = fan;
        this.air = air;
    }

    public MNetAdjust(MNetAdjust original)
    {
        super();

        drive = original.drive;
        mode = original.mode;
        temperature = original.temperature;
        fan = original.fan;
        air = original.air;
    }

    @Override
    public MNetAdjust deepCopy()
    {
        return new MNetAdjust(this);
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

    public void setStateOf(MNetDevice device)
    {
        if (device == null)
        {
            return;
        }

        MNetState state = device.getState();

        if (state == null)
        {
            return;
        }

        setDrive(state.getDrive());
        setMode(state.getMode());
        setTemperature(state.getTemperature());
        setFan(state.getFan());
        setAir(state.getAir());
    }

    public void setPreset(MNetPreset preset)
    {
        if (preset == null)
        {
            return;
        }

        MNetDrive drive = preset.getDrive();

        if (drive != null && drive != MNetDrive.NO_CHANGE)
        {
            setDrive(drive);
        }

        MNetMode mode = preset.getMode();

        if (mode != null && mode.isSelectable() && mode != MNetMode.NO_CHANGE)
        {
            setMode(mode);
        }

        Double temperature = preset.getTemperature();

        if (temperature != null)
        {
            setTemperature(temperature);
        }

        MNetFan fan = preset.getFan();

        if (fan != null && fan != MNetFan.NO_CHANGE)
        {
            setFan(fan);
        }

        MNetAir air = preset.getAir();

        if (air != null && air != MNetAir.NO_CHANGE)
        {
            setAir(air);
        }
    }

    @Override
    public String toString()
    {
        return String
            .format("MNetAdjust [drive=%s, mode=%s, temperature=%s, fan=%s, air=%s]", drive, mode, temperature, fan,
                air);
    }

}
