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

import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.prefs.Prefs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class MNetDevice implements Copyable<MNetDevice>, Persistent
{

    private UUID id = UUID.randomUUID();
    private String name = "";
    private String host = "";
    private Integer address = 0;
    private boolean enabled = true;

    private MNetState state = new MNetState();
    private MNetPreset preset = new MNetPreset();

    private Integer group = null;
    private String model = null;

    public MNetDevice()
    {
        super();
    }

    public MNetDevice(UUID id, String name, String host, Integer address, boolean enabled, MNetState state,
        MNetPreset preset, Integer group, String model)
    {
        super();

        this.id = id;
        this.name = name;
        this.host = host;
        this.address = address;
        this.enabled = enabled;
        this.state = state;
        this.preset = preset;
        this.group = group;
        this.model = model;
    }

    @Override
    public MNetDevice deepCopy()
    {
        return new MNetDevice(id, name, host, address, enabled, Copyable.deepCopy(state), Copyable.deepCopy(preset),
            group, model);
    }

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public MNetDeviceType getType()
    {
        return MNetDeviceType.INSTANCE;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public Integer getAddress()
    {
        return address;
    }

    public void setAddress(Integer address)
    {
        this.address = address;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public MNetState getState()
    {
        return state;
    }

    public void setState(MNetState state)
    {
        this.state = state;
    }

    public MNetPreset getPreset()
    {
        return preset;
    }

    public void setPreset(MNetPreset preset)
    {
        this.preset = preset;
    }

    public Integer getGroup()
    {
        return group;
    }

    public void setGroup(Integer group)
    {
        this.group = group;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public String describe(boolean withHost, MNetStateType stateType)
    {
        StringBuilder builder = new StringBuilder(name);

        if ((withHost) && (host != null))
        {
            String target = host;

            try
            {
                URL url = MNetUtils.toURL(host);

                if (url != null)
                {
                    target = url.toExternalForm();
                }
            }
            catch (MalformedURLException e)
            {
                // ignore
            }

            builder.append(" [").append(target).append(" / ").append(address).append("]");
        }

        if ((stateType != MNetStateType.NONE) && (state != null))
        {
            builder.append(": ").append(state.describe(stateType));
        }

        return builder.toString();
    }

    @Override
    public void read(Prefs prefs)
    {
        id = prefs.getUUID("id", id);
        name = prefs.getString("name", name);
        host = prefs.getString("host", host);
        address = prefs.getInteger("address", 0);
        enabled = prefs.getBoolean("enabled", enabled);

        if (state == null)
        {
            state = new MNetState();
        }

        state.read(prefs.withPrefix("state."));

        if (preset == null)
        {
            preset = new MNetPreset();
        }

        preset.read(prefs.withPrefix("preset."));
    }

    @Override
    public void write(Prefs prefs)
    {
        prefs.setUUID("id", id);
        prefs.setString("name", name);
        prefs.setString("host", host);
        prefs.setInteger("address", address);
        prefs.setBoolean("enabled", enabled);

        state.write(prefs.withPrefix("state."));
        preset.write(prefs.withPrefix("preset."));
    }

    @Override
    public String toString()
    {
        return "MNetDevice [id=" + id + ", name=" + name + ", host=" + host + ", address=" + address + ", enabled="
            + enabled + ", state=" + state + ", preset=" + preset + "]";
    }

}
