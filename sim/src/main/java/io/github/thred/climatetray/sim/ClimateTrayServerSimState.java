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
package io.github.thred.climatetray.sim;

public class ClimateTrayServerSimState
{
    private final String address;
    private final String group;
    private final String model;

    private String drive = "OFF";
    private String mode = "COOL";
    private Double temperature = 22.5;
    private String air = "HORIZONTAL";
    private String fan = "LOW";

    public ClimateTrayServerSimState(String address, String group, String model)
    {
        super();
        this.address = address;
        this.group = group;
        this.model = model;
    }

    public String getAddress()
    {
        return address;
    }

    public String getGroup()
    {
        return group;
    }

    public String getModel()
    {
        return model;
    }

    public String getDrive()
    {
        return drive;
    }

    public void setDrive(String drive)
    {
        this.drive = drive;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
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

    public String getAir()
    {
        return air;
    }

    public void setAir(String air)
    {
        this.air = air;
    }

    public String getFan()
    {
        return fan;
    }

    public void setFan(String fan)
    {
        this.fan = fan;
    }

    @Override
    public String toString()
    {
        return "ClimateTrayServerSimState [address=" + address + ", group=" + group + ", model=" + model + ", drive="
            + drive + ", mode=" + mode + ", temperature=" + temperature + ", air=" + air + ", fan=" + fan + "]";
    }

}
