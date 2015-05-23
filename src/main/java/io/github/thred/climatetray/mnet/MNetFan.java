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
import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetFan
{

    LOW("LOW", "Very Low", "very low fan speed", ClimateTrayImage.ICON_FAN1, ClimateTrayImage.BACKGROUND_FAN1),
    MID1("MID1", "Low", "low fan speed", ClimateTrayImage.ICON_FAN2, ClimateTrayImage.BACKGROUND_FAN2),
    MID2("MID2", "High", "high fan speed", ClimateTrayImage.ICON_FAN3, ClimateTrayImage.BACKGROUND_FAN3),
    HIGH("HIGH", "Very High", "very high fan speed", ClimateTrayImage.ICON_FAN4, ClimateTrayImage.BACKGROUND_FAN4);

    public static MNetFan valueOfKey(String key)
    {
        if ((key == null) || ("*".equals(key)))
        {
            return null;
        }

        for (MNetFan fan : values())
        {
            if (key.equals(fan.getKey()))
            {
                return fan;
            }
        }

        ClimateTray.LOG.error("Failed to parse fan key \"%s\"", key);

        return null;
    }

    private final String key;
    private final String label;
    private final String description;
    private final ClimateTrayImage image;
    private final ClimateTrayImage backgroundImage;

    private MNetFan(String key, String label, String description, ClimateTrayImage image,
        ClimateTrayImage backgroundImage)
    {
        this.key = key;
        this.label = label;
        this.description = description;
        this.image = image;
        this.backgroundImage = backgroundImage;
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

    public ClimateTrayImage getImage()
    {
        return image;
    }

    public ClimateTrayImage getBackgroundImage()
    {
        return backgroundImage;
    }
    
    @Override
    public String toString()
    {
        return getLabel();
    }

    public static String labelOf(MNetFan mode)
    {
        return (mode != null) ? mode.getLabel() : null;
    }

    public static String descriptionOf(MNetFan mode)
    {
        return (mode != null) ? mode.getDescription() : null;
    }

}
