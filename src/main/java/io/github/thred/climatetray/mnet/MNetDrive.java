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

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetDrive
{

    NO_CHANGE(null, "Do not change", null, ClimateTrayImage.ICON_JOKER, null),
    ON("ON", "On", "turn on", ClimateTrayImage.ICON_ON, null),
    OFF("OFF", "Off", "turn off", ClimateTrayImage.ICON_OFF, ClimateTrayImage.BACKGROUND_OFF);

    public static MNetDrive valueOfKey(String key)
    {
        if ((key == null) || ("*".equals(key)))
        {
            return null;
        }

        for (MNetDrive ec : values())
        {
            if (key.equals(ec.getKey()))
            {
                return ec;
            }
        }

        LOG.error("Failed to parse drive key \"%s\"", key);

        return null;
    }

    private final String key;
    private final String label;
    private final String description;
    private final ClimateTrayImage image;
    private final ClimateTrayImage backgroundImage;

    private MNetDrive(String key, String label, String description, ClimateTrayImage image,
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

    public static String labelOf(MNetDrive drive)
    {
        return (drive != null) ? drive.getLabel() : null;
    }

    public static String descriptionOf(MNetDrive drive)
    {
        return (drive != null) ? drive.getDescription() : null;
    }
}
