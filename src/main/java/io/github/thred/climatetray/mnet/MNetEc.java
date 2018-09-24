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

public enum MNetEc
{

    NONE(null, "Not Used"),
    EC_1("1", "EC #1"),
    EC_2("2", "EC #2"),
    EC_3("3", "EC #3");

    public static MNetEc valueOfKey(String key)
    {
        if ((key == null) || ("*".equals(key)))
        {
            return null;
        }

        for (MNetEc ec : values())
        {
            if (key.equals(ec.getKey()))
            {
                return ec;
            }
        }

        LOG.error("Failed to parse EC key \"%s\"", key);

        return null;
    }

    private final String key;
    private final String label;

    private MNetEc(String key, String label)
    {
        this.key = key;
        this.label = label;
    }

    public String getKey()
    {
        return key;
    }

    public String getLabel()
    {
        return label;
    }

    @Override
    public String toString()
    {
        return getLabel();
    }

    public static String labelOf(MNetEc mode)
    {
        return (mode != null) ? mode.getLabel() : null;
    }

}
