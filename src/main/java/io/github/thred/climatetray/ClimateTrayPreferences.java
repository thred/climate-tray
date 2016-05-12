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

    public static final int VERSION = 2;

    private final ClimateTrayProxySettings proxySettings = new ClimateTrayProxySettings();
    private final List<MNetDevice> devices = new ArrayList<MNetDevice>();
    private final List<MNetPreset> presets = new ArrayList<MNetPreset>();

    private int version = 0;
    private TemperatureUnit temperatureUnit = TemperatureUnit.CELSIUS;
    private double updatePeriodInMinutes = 1;
    private boolean versionCheckEnabled = true;
    private boolean trayIconEnabled = true;

    public ClimateTrayPreferences()
    {
        super();
    }

    public ClimateTrayProxySettings getProxySettings()
    {
        return proxySettings;
    }

    public MNetDevice getDevice(UUID id)
    {
        return devices.stream().filter((device) -> Objects.equals(id, device.getId())).findFirst().orElse(null);
    }

    public List<MNetDevice> getDevices()
    {
        return devices;
    }

    public boolean isAnyDeviceSelected()
    {
        return devices.stream().filter(device -> device.isEnabled() && device.isSelected()).count() > 0;
    }

    public boolean isAnyDeviceSelectedAndWorking()
    {
        return devices.stream().filter(device -> device.isEnabled() && device.isSelectedAndWorking()).count() > 0;
    }

    public MNetPreset getPreset(UUID id)
    {
        return presets.stream().filter((device) -> Objects.equals(id, device.getId())).findFirst().orElse(null);
    }

    public List<MNetPreset> getPresets()
    {
        return presets;
    }

    public int getVersion()
    {
        return version;
    }

    public void setVersion(int version)
    {
        this.version = version;
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

    public boolean isVersionCheckEnabled()
    {
        return versionCheckEnabled;
    }

    public void setVersionCheckEnabled(boolean versionCheckEnabled)
    {
        this.versionCheckEnabled = versionCheckEnabled;
    }

    public boolean isTrayIconEnabled()
    {
        return trayIconEnabled;
    }

    public void setTrayIconEnabled(boolean trayIconEnabled)
    {
        this.trayIconEnabled = trayIconEnabled;
    }

    @Override
    public void read(Prefs prefs)
    {
        version = prefs.getInteger("version", version);

        proxySettings.read(prefs);

        Persistent.readList(prefs, "device", devices, MNetDevice::new);
        Persistent.readList(prefs, "preset", presets, MNetPreset::new);

        temperatureUnit = prefs.getEnum(TemperatureUnit.class, "temperatureUnit", temperatureUnit);
        updatePeriodInMinutes = prefs.getDouble("updatePeriodInMinutes", updatePeriodInMinutes);
        versionCheckEnabled = prefs.getBoolean("versionCheckEnabled", versionCheckEnabled);
        trayIconEnabled = prefs.getBoolean("trayIconEnabled", trayIconEnabled);
    }

    @Override
    public void write(Prefs prefs)
    {
        version = VERSION;

        prefs.setInteger("version", version);

        proxySettings.write(prefs);

        Persistent.writeList(prefs, "device", devices);
        Persistent.writeList(prefs, "preset", presets);

        prefs.setEnum("temperatureUnit", temperatureUnit);
        prefs.setDouble("updatePeriodInMinutes", updatePeriodInMinutes);
        prefs.setBoolean("versionCheckEnabled", versionCheckEnabled);
        prefs.setBoolean("trayIconEnabled", trayIconEnabled);
    }

    @Override
    public String toString()
    {
        return "ClimateTrayPreferences [proxySettings=" + proxySettings + ", devices=" + devices + ", presets="
            + presets + ", temperatureUnit=" + temperatureUnit + ", updatePeriodInMinutes=" + updatePeriodInMinutes
            + ", trayIconEnabled=" + trayIconEnabled + "]";
    }

}
