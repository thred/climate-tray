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

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetInstallation
{

    STANDING("Standing", ClimateTrayImage.ICON_VERTICAL),
    HANGING("Hanging", ClimateTrayImage.ICON_HORIZONTAL);

    private final String label;
    private final ClimateTrayImage image;

    private MNetInstallation(String label, ClimateTrayImage image)
    {
        this.label = label;
        this.image = image;
    }

    public String getLabel()
    {
        return label;
    }

    public ClimateTrayImage getImage()
    {
        return image;
    }

    public MNetAir translate(MNetAir air)
    {
        if (air == null)
        {
            return null;
        }

        return air.translate(this);
    }

    @Override
    public String toString()
    {
        return getLabel();
    }

    public static String labelOf(MNetInstallation drive)
    {
        return (drive != null) ? drive.getLabel() : null;
    }

}
