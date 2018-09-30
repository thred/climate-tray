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

import static io.github.thred.climatetray.ClimateTray.*;

import java.util.Arrays;

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetMode
{

    NO_CHANGE(null, "Do not change", null, null, ClimateTrayImage.ICON_JOKER, null, true, 17, 30, true, true),
    FAN("FAN", "Fan Only", "blowing", "blowing", ClimateTrayImage.ICON_FAN, ClimateTrayImage.BACKGROUND_FAN1, true,
        true, true),
    COOL("COOL", "Cool", "cool down", "cooling down to %s", ClimateTrayImage.ICON_COOL,
        ClimateTrayImage.BACKGROUND_COOL, true, 19, 30, true, true),
    HEAT("HEAT", "Heat", "heat up", "heating up to %s", ClimateTrayImage.ICON_HEAT, ClimateTrayImage.BACKGROUND_HEAT,
        true, 17, 28, true, true),
    AUTO("AUTO", "Automatic", "auto", "keeping %s", ClimateTrayImage.ICON_AUTO, ClimateTrayImage.BACKGROUND_AUTO, true,
        19, 28, true, true),
    AUTO_COOL("AUTOCOOL", "Automatic (cooling)", "auto\u25bc", "cooling down to %s", ClimateTrayImage.ICON_AUTO,
        ClimateTrayImage.BACKGROUND_AUTO_COOL, false, 19, 28, true, true),
    AUTO_HEAT("AUTOHEAT", "Automatic (heating)", "auto\u25b2", "heating up to %s", ClimateTrayImage.ICON_AUTO,
        ClimateTrayImage.BACKGROUND_AUTO_HEAT, false, 19, 28, true, true),
    DRY("DRY", "Dry", "drying", "drying at %s", ClimateTrayImage.ICON_DRY, ClimateTrayImage.BACKGROUND_DRY, true, true,
        true);

    public static MNetMode valueOfKey(String key)
    {
        if ((key == null) || ("*".equals(key)))
        {
            return null;
        }

        for (MNetMode mode : values())
        {
            if (key.equals(mode.getKey()))
            {
                return mode;
            }
        }

        LOG.error("Failed to parse mode key \"%s\"", key);

        return null;
    }

    private static MNetMode[] selectableValues;

    private final String key;
    private final String label;
    private final String description;
    private final String action;
    private final ClimateTrayImage image;
    private final ClimateTrayImage backgroundImage;
    private final boolean selectable;
    private final boolean temperatureEnabled;
    private final int minimumTemperature;
    private final int maximumTemperature;
    private final boolean fanEnabled;
    private final boolean airEnabled;

    private MNetMode(String key, String label, String description, String action, ClimateTrayImage image,
        ClimateTrayImage backgroundImage, boolean selectable, boolean fanEnabled, boolean airEnabled)
    {
        this(key, label, description, action, image, backgroundImage, selectable, false, 0, 0, fanEnabled, airEnabled);
    }

    private MNetMode(String key, String label, String description, String action, ClimateTrayImage image,
        ClimateTrayImage backgroundImage, boolean selectable, int minTemperature, int maxTemperature,
        boolean fanEnabled, boolean airEnabled)
    {
        this(key, label, description, action, image, backgroundImage, selectable, true, minTemperature, maxTemperature,
            fanEnabled, airEnabled);
    }

    private MNetMode(String key, String label, String description, String action, ClimateTrayImage image,
        ClimateTrayImage backgroundImage, boolean selectable, boolean temperatureEnabled, int minimumTemperature,
        int maximumTemperature, boolean fanEnabled, boolean airEnabled)
    {
        this.key = key;
        this.label = label;
        this.description = description;
        this.action = action;
        this.image = image;
        this.backgroundImage = backgroundImage;
        this.temperatureEnabled = temperatureEnabled;
        this.selectable = selectable;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.fanEnabled = fanEnabled;
        this.airEnabled = airEnabled;
    }

    public String getKey()
    {
        return key;
    }

    public String getLabel()
    {
        return label;
    }

    public String getDescription()
    {
        return description;
    }

    public String getAction()
    {
        return action;
    }

    public ClimateTrayImage getImage()
    {
        return image;
    }

    public ClimateTrayImage getBackgroundImage()
    {
        return backgroundImage;
    }

    public boolean isSelectable()
    {
        return selectable;
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

    @Override
    public String toString()
    {
        return getLabel();
    }

    public static MNetMode[] selectableValues()
    {
        if (selectableValues != null)
        {
            return selectableValues;
        }

        return selectableValues =
            Arrays.stream(values()).filter(item -> item.isSelectable()).toArray(size -> new MNetMode[size]);
    }

    public static String labelOf(MNetMode mode)
    {
        return (mode != null) ? mode.getLabel() : null;
    }

    public static String descriptionOf(MNetMode mode)
    {
        return (mode != null) ? mode.getDescription() : null;
    }

}
