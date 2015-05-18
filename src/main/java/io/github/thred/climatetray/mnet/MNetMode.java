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

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetMode
{

    OFF(null, "OFF", "Off", "Off", ClimateTrayImage.ICON_OFF, ClimateTrayImage.BACKGROUND_OFF, false, false),
    FAN("FAN", "ON", "Fan", "Blowing", ClimateTrayImage.ICON_FAN, ClimateTrayImage.BACKGROUND_FAN1, true, true),
    COOL("COOL", "ON", "Cool", "Cooling", ClimateTrayImage.ICON_COOL, ClimateTrayImage.BACKGROUND_COOL, 19, 30, true,
        true),
    HEAT("HEAT", "ON", "Heat", "Heating", ClimateTrayImage.ICON_HEAT, ClimateTrayImage.BACKGROUND_HEAT, 17, 28, true,
        true),
    AUTO("AUTO", "ON", "Automatic", "Auto", ClimateTrayImage.ICON_AUTO, ClimateTrayImage.BACKGROUND_AUTO, 17, 28, true,
        true),
    DRY("DRY", "ON", "Dry", "Drying", ClimateTrayImage.ICON_DRY, ClimateTrayImage.BACKGROUND_DRY, true, true);

    private final String key;
    private final String driveKey;
    private final String label;
    private final String description;
    private final ClimateTrayImage image;
    private final ClimateTrayImage backgroundImage;
    private final boolean temperatureEnabled;
    private final int minimumTemperature;
    private final int maximumTemperature;
    private final boolean fanEnabled;
    private final boolean airEnabled;

    private MNetMode(String key, String driveKey, String label, String description, ClimateTrayImage image,
        ClimateTrayImage backgroundImage, boolean fanEnabled, boolean airEnabled)
    {
        this(key, driveKey, label, description, image, backgroundImage, false, 0, 0, fanEnabled, airEnabled);
    }

    private MNetMode(String key, String driveKey, String label, String description, ClimateTrayImage image,
        ClimateTrayImage backgroundImage, int minTemperature, int maxTemperature, boolean fanEnabled, boolean airEnabled)
    {
        this(key, driveKey, label, description, image, backgroundImage, true, minTemperature, maxTemperature,
            fanEnabled, airEnabled);
    }

    private MNetMode(String key, String driveKey, String label, String description, ClimateTrayImage image,
        ClimateTrayImage backgroundImage, boolean temperatureEnabled, int minimumTemperature, int maximumTemperature,
        boolean fanEnabled, boolean airEnabled)
    {
        this.key = key;
        this.driveKey = driveKey;
        this.label = label;
        this.description = description;
        this.image = image;
        this.backgroundImage = backgroundImage;
        this.temperatureEnabled = temperatureEnabled;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.fanEnabled = fanEnabled;
        this.airEnabled = airEnabled;
    }

    public String getKey()
    {
        return key;
    }

    public String getDriveKey()
    {
        return driveKey;
    }

    public String getLabel()
    {
        return label;
    }

    public String getDescription()
    {
        return description;
    }

    public ClimateTrayImage getImage()
    {
        return image;
    }

    public ClimateTrayImage getBackgroundImage()
    {
        return backgroundImage;
    }

    public boolean isTemperatureEnabled()
    {
        return temperatureEnabled;
    }

    public int getMinimumTemperature()
    {
        return minimumTemperature;
    }

    public int getMaximumTemperature()
    {
        return maximumTemperature;
    }

    public boolean isFanEnabled()
    {
        return fanEnabled;
    }

    public boolean isAirEnabled()
    {
        return airEnabled;
    }

}
