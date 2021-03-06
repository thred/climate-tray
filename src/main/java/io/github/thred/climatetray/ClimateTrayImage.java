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
package io.github.thred.climatetray;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public enum ClimateTrayImage
{

    BACKGROUND,
    BACKGROUND_SELECTED,
    BACKGROUND_AUTO,
    BACKGROUND_AUTO_COOL,
    BACKGROUND_AUTO_HEAT,
    BACKGROUND_COOL,
    BACKGROUND_DRY,
    BACKGROUND_FAN1,
    BACKGROUND_FAN2,
    BACKGROUND_FAN3,
    BACKGROUND_FAN4,
    BACKGROUND_HEAT,
    BACKGROUND_OFF,
    BACKGROUND_TEMPERATURE,
    CHAR__,
    CHAR_0,
    CHAR_1,
    CHAR_2,
    CHAR_3,
    CHAR_4,
    CHAR_5,
    CHAR_6,
    CHAR_7,
    CHAR_8,
    CHAR_9,
    FOREGROUND_WARN,
    FOREGROUND_ERROR,
    ICON,
    ICON_AUTO,
    ICON_COOL,
    ICON_DIR1,
    ICON_DIR2,
    ICON_DIR3,
    ICON_DIR4,
    ICON_DIR5,
    ICON_DRY,
    ICON_FAN,
    ICON_FAN1,
    ICON_FAN2,
    ICON_FAN3,
    ICON_FAN4,
    ICON_HEAT,
    ICON_HORIZONTAL,
    ICON_JOKER,
    ICON_LESS,
    ICON_MORE,
    ICON_ON,
    ICON_OFF,
    ICON_SWING,
    ICON_THERMOMETER,
    ICON_VERTICAL,
    ICON_ERROR,
    ICON_WARNING,
    ICON_INFO,
    ICON_DEBUG,
    ICON_UP,
    ICON_DOWN,
    ICON_REMOVE,
    ICON_ADD,
    ICON_EDIT,
    SETTINGS;

    private ClimateTrayImage()
    {
        // intentionally left blank
    }

    public Image getImage(ClimateTrayImageState state, int size)
    {
        String resourceName = getResourceName(size);
        String imageName = resourceName + state.getPostfix();
        Image image = ClimateTrayCache.getImage(imageName);

        if (image != null)
        {
            return image;
        }

        Image backgroundImage = null;
        Image foregroundImage = ClimateTrayCache.getImage(resourceName);

        switch (state)
        {
            case NONE:
                // do nothing
                break;

            case HIGHLIGHT:
                backgroundImage = BACKGROUND_SELECTED.getImage(ClimateTrayImageState.NONE, size);
                break;

            case DEFAULT:
                backgroundImage = BACKGROUND.getImage(ClimateTrayImageState.NONE, size);
                break;

            default:
                throw new UnsupportedOperationException("State not supported: " + state);
        }

        BufferedImage combinedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = combinedImage.createGraphics();

        if (backgroundImage != null)
        {
            g.drawImage(backgroundImage, 0, 0, null);
        }

        g.drawImage(foregroundImage, 0, 0, null);

        return ClimateTrayCache.addImage(imageName, combinedImage);
    }

    public List<? extends Image> getImages(ClimateTrayImageState state, Integer... sizes)
    {
        List<Image> results = new ArrayList<>();

        for (Integer size : sizes)
        {
            Image image = getImage(state, size);

            if (image != null)
            {
                results.add(image);
            }
        }

        return results;
    }

    public Icon getIcon(ClimateTrayImageState state, int size)
    {
        String resourceName = getResourceName(size);
        String iconName = resourceName + state.getPostfix();
        Icon icon = ClimateTrayCache.getIcon(iconName);

        if (icon != null)
        {
            return icon;
        }

        return ClimateTrayCache.addIcon(iconName, new ImageIcon(getImage(state, size)));
    }

    private String getResourceName(int size)
    {
        return name().toLowerCase().replace('_', '-') + "-" + size + ".png";
    }

    public static ClimateTrayImage getCharImage(char ch)
    {
        switch (ch)
        {
            case '0':
                return ClimateTrayImage.CHAR_0;

            case '1':
                return ClimateTrayImage.CHAR_1;

            case '2':
                return ClimateTrayImage.CHAR_2;

            case '3':
                return ClimateTrayImage.CHAR_3;

            case '4':
                return ClimateTrayImage.CHAR_4;

            case '5':
                return ClimateTrayImage.CHAR_5;

            case '6':
                return ClimateTrayImage.CHAR_6;

            case '7':
                return ClimateTrayImage.CHAR_7;

            case '8':
                return ClimateTrayImage.CHAR_8;

            case '9':
                return ClimateTrayImage.CHAR_9;

            case '-':
                return ClimateTrayImage.CHAR__;

            default:
                throw new UnsupportedOperationException("Unsupported char: " + ch);
        }
    }
}
