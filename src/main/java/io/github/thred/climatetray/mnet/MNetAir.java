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

public enum MNetAir
{

    NO_CHANGE(null, "Do not change", "Do not change", null, ClimateTrayImage.ICON_JOKER),
    HORIZONTAL("HORIZONTAL", "Horizontal", "Horiz.", "fins horizontal", ClimateTrayImage.ICON_DIR1),
    MID1("MID1", "Slightly Inclined", "Incl. A", "fins slightly inclined", ClimateTrayImage.ICON_DIR2),
    MID2("MID2", "Strongly Inclined", "Incl. B", "fins strongly inclined", ClimateTrayImage.ICON_DIR3),
    VERTICAL("VERTICAL", "Vertical", "Vert.", "fins vertical", ClimateTrayImage.ICON_DIR4),
    SWING("SWING", "Swing", "Swing", "fins swinging", ClimateTrayImage.ICON_SWING);

    public static MNetAir valueOfKey(String key)
    {
        if ((key == null) || ("*".equals(key)))
        {
            return null;
        }

        for (MNetAir air : values())
        {
            if (key.equals(air.getKey()))
            {
                return air;
            }
        }

        LOG.error("Failed to parse air key \"%s\"", key);

        return null;
    }

    private final String key;
    private final String label;
    private final String shortLabel;
    private final String description;
    private final ClimateTrayImage image;

    private MNetAir(String key, String label, String shortLabel, String description, ClimateTrayImage image)
    {
        this.key = key;
        this.label = label;
        this.shortLabel = shortLabel;
        this.description = description;
        this.image = image;
    }

    public String getKey()
    {
        return key;
    }

    public String getLabel()
    {
        return label;
    }

    public String getShortLabel()
    {
        return shortLabel;
    }

    public String getDescription()
    {
        return description;
    }

    public ClimateTrayImage getImage()
    {
        return image;
    }

    public MNetAir translate(MNetInstallation installation)
    {
        if (installation == MNetInstallation.HANGING)
        {
            return this;
        }

        switch (this)
        {
            case HORIZONTAL:
                return VERTICAL;

            case MID1:
                return MID2;

            case MID2:
                return MID1;

            case VERTICAL:
                return HORIZONTAL;

            default:
                return this;
        }
    }

    @Override
    public String toString()
    {
        return getLabel();
    }

    public static String labelOf(MNetAir mode)
    {
        return (mode != null) ? mode.getLabel() : null;
    }

    public static String descriptionOf(MNetAir mode)
    {
        return (mode != null) ? mode.getDescription() : null;
    }

}
