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
import io.github.thred.climatetray.util.Utils;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class MNetState implements Copyable<MNetState>
{

    private MNetDrive drive = MNetDrive.OFF;
    private MNetMode mode = MNetMode.AUTO;
    private Double temperature = null;
    private Double thermometer = null;
    private MNetFan fan = null;
    private MNetAir air = null;

    public MNetState()
    {
        super();
    }

    public MNetState(MNetDrive drive, MNetMode mode, Double temperature, Double thermometer, MNetFan fan, MNetAir air)
    {
        super();

        this.drive = drive;
        this.mode = mode;
        this.temperature = temperature;
        this.thermometer = thermometer;
        this.fan = fan;
        this.air = air;
    }

    @Override
    public MNetState deepCopy()
    {
        return new MNetState(drive, mode, temperature, thermometer, fan, air);
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

    public Double getThermometer()
    {
        return thermometer;
    }

    public void setThermometer(Double thermometer)
    {
        this.thermometer = thermometer;
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
        return new ImageIcon(createImage(state, size));
    }

    public Image createImage(ClimateTrayImageState state, int size)
    {
        MNetMode mode = getMode();
        Double thermometer = getThermometer();
        MNetFan fan = getFan();
        MNetAir air = getAir();

        if (drive == MNetDrive.OFF)
        {
            mode = null;
            fan = null;
            air = null;
        }

        return MNetUtils.createImage(state, size, drive, mode, thermometer, fan, air);
    }

    public String describe()
    {
        boolean on = drive == MNetDrive.ON;
        String power = ((on) && (mode != null)) ? mode.getDescription() : MNetDrive.labelOf(drive);
        String temp =
            (on) ? Utils.combine(" -> ", PREFERENCES.getTemperatureUnit().format(thermometer), PREFERENCES
                .getTemperatureUnit().format(temperature)) : PREFERENCES.getTemperatureUnit().format(thermometer);

        String result = Utils.combine(" ", power, Utils.surround("(", temp, ")"));

        if (on)
        {
            result =
                Utils.combine(" ", result,
                    Utils.combine(", ", MNetFan.descriptionOf(fan), MNetAir.descriptionOf(air)));
        }

        return result;
    }

    @Override
    public String toString()
    {
        return "MNetState [drive=" + drive + ", mode=" + mode + ", temperature=" + temperature + ", thermometer="
            + thermometer + ", fan=" + fan + ", air=" + air + "]";
    }

}
