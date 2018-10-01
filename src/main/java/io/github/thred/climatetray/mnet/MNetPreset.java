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
package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.ClimateTray.*;

import java.util.UUID;

import javax.swing.Icon;

import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.prefs.Prefs;

public class MNetPreset implements Copyable<MNetPreset>, Persistent
{

    private UUID id = UUID.randomUUID();
    private MNetDrive drive = MNetDrive.NO_CHANGE;
    private MNetMode mode = MNetMode.NO_CHANGE;
    private Double temperature = null;
    private MNetFan fan = MNetFan.NO_CHANGE;
    private MNetAir air = MNetAir.NO_CHANGE;
    private boolean selected = false;

    public MNetPreset()
    {
        super();
    }

    public MNetPreset(UUID id, MNetDrive drive, MNetMode mode, Double temperature, MNetFan fan, MNetAir air,
        boolean selected)
    {
        super();

        this.id = id;
        this.drive = drive;
        this.mode = mode;
        this.temperature = temperature;
        this.fan = fan;
        this.air = air;
        this.selected = selected;
    }

    @Override
    public MNetPreset deepCopy()
    {
        return new MNetPreset(id, drive, mode, temperature, fan, air, selected);
    }

    public void set(MNetPreset preset)
    {
        if (preset == null)
        {
            return;
        }

        drive = preset.drive;
        mode = preset.mode;
        temperature = preset.temperature;
        fan = preset.fan;
        air = preset.air;
        selected = preset.selected;
    }

    public void set(MNetDevice device)
    {
        if (device == null)
        {
            return;
        }

        set(device.getState());
    }

    public void set(MNetState state)
    {
        if (state == null)
        {
            return;
        }

        drive = state.getDrive();

        switch (state.getMode())
        {
            case AUTO:
            case AUTO_COOL:
            case AUTO_HEAT:
                mode = MNetMode.AUTO;
                break;

            default:
                mode = state.getMode();
                break;
        }

        temperature = state.getTemperature();
        fan = state.getFan();
        air = state.getAir();
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
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

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public boolean isValid()
    {
        return drive != MNetDrive.NO_CHANGE
            || mode != MNetMode.NO_CHANGE
            || temperature != null
            || air != MNetAir.NO_CHANGE
            || fan != MNetFan.NO_CHANGE;
    }

    public Icon createIcon(ClimateTrayImageState state, int size)
    {
        MNetDrive drive = getDrive();
        MNetMode mode = getMode();
        Double temperature = getTemperature();
        MNetFan fan = getFan();
        MNetAir air = getAir();

        if (drive == MNetDrive.NO_CHANGE)
        {
            drive = null;
        }
        else if (drive == MNetDrive.OFF)
        {
            mode = null;
            temperature = null;
            fan = null;
            air = null;
        }

        if (mode == MNetMode.NO_CHANGE)
        {
            mode = null;
        }

        if ((mode != null) && (!mode.isTemperatureEnabled()))
        {
            temperature = null;
        }

        if ((mode != null) && (!mode.isFanEnabled()))
        {
            fan = null;
        }

        if ((mode != null) && (!mode.isAirEnabled()))
        {
            air = null;
        }

        if (fan == MNetFan.NO_CHANGE)
        {
            fan = null;
        }

        if (air == MNetAir.NO_CHANGE)
        {
            air = null;
        }

        return MNetUtils.createIcon(state, size, drive, mode, temperature, fan, air, false, false);
    }

    public String describe()
    {
        return Utils
            .sentence(Utils
                .combine(" | ", MNetDrive.descriptionOf(drive), MNetMode.descriptionOf(mode),
                    PREFERENCES.getTemperatureUnit().format(temperature), MNetFan.descriptionOf(fan),
                    MNetAir.descriptionOf(air)));
    }

    @Override
    public void read(Prefs prefs)
    {
        id = prefs.getUUID("id", id);
        drive = prefs.getEnum(MNetDrive.class, "drive", drive);
        mode = prefs.getEnum(MNetMode.class, "mode", mode);
        temperature = prefs.getDouble("temperature", temperature);
        fan = prefs.getEnum(MNetFan.class, "fan", fan);
        air = prefs.getEnum(MNetAir.class, "air", air);

        selected = false;
    }

    @Override
    public void write(Prefs prefs)
    {
        prefs.setUUID("id", id);
        prefs.setEnum("drive", drive);
        prefs.setEnum("mode", mode);
        prefs.setDouble("temperature", temperature);
        prefs.setEnum("fan", fan);
        prefs.setEnum("air", air);
    }

    @Override
    public String toString()
    {
        return String
            .format("MNetPreset [id=%s, drive=%s, mode=%s, temperature=%s, fan=%s, air=%s, selected=%s]", id, drive,
                mode, temperature, fan, air, selected);
    }

}
