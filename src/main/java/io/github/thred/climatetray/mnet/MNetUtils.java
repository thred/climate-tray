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
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class MNetUtils
{

    public static URL toURL(String host) throws MalformedURLException
    {
        if (Utils.isBlank(host))
        {
            return null;
        }

        host = host.trim();

        if ((!host.startsWith("http://")) && (!host.startsWith("https://")))
        {
            host = "http://" + host;
        }

        while (host.endsWith("/"))
        {
            host = host.substring(0, host.length() - 1);
        }

        URL url = new URL(host);
        String path = url.getPath();

        if (Utils.isBlank(path))
        {
            host += "/servlet/MIMEReceiveServlet";

            url = new URL(host);
        }

        return url;
    }

    public static Icon createIcon(ClimateTrayImageState state, int size, MNetMode mode, MNetFan fan,
        Double temperature, MNetAir air)
    {
        return new ImageIcon(createImage(state, size, mode, fan, temperature, air));
    }

    public static Image createImage(ClimateTrayImageState state, int size, MNetMode mode, MNetFan fan,
        Double temperature, MNetAir air)
    {
        if (mode == null)
        {
            mode = MNetMode.OFF;
        }

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        boolean temperatureEnabled = temperature != null;//mode.isTemperatureEnabled();

        if (mode == MNetMode.FAN)
        {
            g.drawImage((temperatureEnabled) ? fan.getBackgroundImage().getImage(state, size) : fan.getImage()
                .getImage(state, size), 0, 0, null);
        }
        else
        {
            g.drawImage((temperatureEnabled) ? mode.getBackgroundImage().getImage(state, size) : mode.getImage()
                .getImage(state, size), 0, 0, null);
        }

        if ((temperature != null) && temperatureEnabled)
        {
            String value =
                String
                    .valueOf(Math.round(ClimateTray.PREFERENCES.getTemperatureUnit().convertFromCelsius(temperature)));
            int x = (int) (size * 0.45);

            for (int i = value.length() - 1; i >= 0; i -= 1)
            {
                Image charImage =
                    ClimateTrayImage.getCharImage(value.charAt(i)).getImage(ClimateTrayImageState.NONE, size);

                g.drawImage(charImage, x, 0, null);

                x -= charImage.getWidth(null) * 0.8;
            }
        }

        return image;
    }

}
