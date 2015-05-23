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

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetAir
{

    NO_CHANGE(null, "Do not change", null, ClimateTrayImage.ICON_JOKER),
    HORIZONTAL("HORIZONTAL", "Horizontal", "fins horizontal", ClimateTrayImage.ICON_DIR1),
    MID1("MID1", "Slightly Inclined", "fins slightly inclined", ClimateTrayImage.ICON_DIR2),
    MID2("MID2", "Stronly Inclined", "fins stronly inclined", ClimateTrayImage.ICON_DIR3),
    VERTICAL("VERTICAL", "Vertical", "fins vertical", ClimateTrayImage.ICON_DIR4),
    SWING("SWING", "Swing", "fins swining", ClimateTrayImage.ICON_SWING);

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
    private final String description;
    private final ClimateTrayImage image;

    private MNetAir(String key, String label, String description, ClimateTrayImage image)
    {
        this.key = key;
        this.label = label;
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

    public String getDescription()
    {
        return description;
    }

    public ClimateTrayImage getImage()
    {
        return image;
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
