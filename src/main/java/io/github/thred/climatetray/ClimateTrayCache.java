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
package io.github.thred.climatetray;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ClimateTrayCache
{

    private static final Map<String, Image> images = new HashMap<>(0);
    private static final Map<String, Icon> icons = new HashMap<>(0);

    public static Image getImage(String resourceName)
    {
        Image image = images.get(resourceName);

        if (image != null)
        {
            return image;
        }

        URL resourceUrl = getResourceUrl(resourceName);

        if (resourceUrl == null)
        {
            return null;
        }

        try
        {
            image = ImageIO.read(resourceUrl);
        }
        catch (IOException e)
        {
            throw new ClimateTrayException("Could not read image: " + resourceName, e);
        }

        return addImage(resourceName, image);
    }

    public static Image addImage(String resourceName, Image image)
    {
        images.put(resourceName, image);

        return image;
    }

    public static Icon getIcon(String resourceName)
    {
        Icon icon = icons.get(resourceName);

        if (icon != null)
        {
            return icon;
        }

        Image image = getImage(resourceName);

        if (image == null)
        {
            return null;
        }

        icon = new ImageIcon(image);

        return addIcon(resourceName, icon);
    }

    public static Icon addIcon(String resourceName, Icon icon)
    {
        icons.put(resourceName, icon);

        return icon;
    }

    public static URL getResourceUrl(String resourceName)
    {
        return ClimateTray.class.getResource(resourceName);
    }

    private ClimateTrayCache()
    {
        super();
    }

}
