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
package io.github.thred.climatetray;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.TemperatureUnit;
import io.github.thred.climatetray.util.prefs.Prefs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ClimateTrayPreferences implements Persistent
{

    private final List<MNetDevice> devices = new ArrayList<MNetDevice>();
    private final List<MNetPreset> presets = new ArrayList<MNetPreset>();

    private TemperatureUnit temperatureUnit = TemperatureUnit.CELSIUS;
    private double updatePeriodInMinutes = 1;

    public ClimateTrayPreferences()
    {
        super();
    }

    public MNetDevice getDevice(UUID id)
    {
        return devices.stream().filter((device) -> Objects.equals(id, device.getId())).findFirst().orElse(null);
    }

    public List<MNetDevice> getDevices()
    {
        return devices;
    }

    public MNetPreset getPreset(UUID id)
    {
        return presets.stream().filter((device) -> Objects.equals(id, device.getId())).findFirst().orElse(null);
    }

    public List<MNetPreset> getPresets()
    {
        return presets;
    }

    public TemperatureUnit getTemperatureUnit()
    {
        return temperatureUnit;
    }

    public void setTemperatureUnit(TemperatureUnit temperatureUnit)
    {
        this.temperatureUnit = temperatureUnit;
    }

    public double getUpdatePeriodInMinutes()
    {
        return updatePeriodInMinutes;
    }

    public void setUpdatePeriodInMinutes(double updatePeriodInMinutes)
    {
        this.updatePeriodInMinutes = updatePeriodInMinutes;
    }

    @Override
    public void read(Prefs prefs)
    {
        Persistent.readList(prefs, "device", devices, MNetDevice::new);
        Persistent.readList(prefs, "preset", presets, MNetPreset::new);

        temperatureUnit = prefs.getEnum(TemperatureUnit.class, "temperatureUnit", temperatureUnit);
        updatePeriodInMinutes = prefs.getDouble("updatePeriodInMinutes", updatePeriodInMinutes);
    }

    @Override
    public void write(Prefs prefs)
    {
        Persistent.writeList(prefs, "device", devices);
        Persistent.writeList(prefs, "preset", presets);

        prefs.setEnum("temperatureUnit", temperatureUnit);
        prefs.setDouble("updatePeriodInMinutes", updatePeriodInMinutes);
    }
}
