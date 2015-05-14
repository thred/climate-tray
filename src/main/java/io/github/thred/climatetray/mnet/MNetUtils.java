package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class MNetUtils
{

    public static Icon createIcon(ClimateTrayImageState state, int size, MNetMode mode, MNetFan fan,
        Double temperature, MNetAir air)
    {
        if (mode == null)
        {
            mode = MNetMode.OFF;
        }

        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        if (mode == MNetMode.FAN)
        {
            g.drawImage(fan.getBackgroundImage().getImage(state, size), 0, 0, null);
        }
        else if ((mode == MNetMode.OFF) && (temperature == null))
        {
            g.drawImage(mode.getImage().getImage(state, size), 0, 0, null);
        }
        else
        {
            g.drawImage(mode.getBackgroundImage().getImage(state, size), 0, 0, null);
        }

        if (temperature != null)
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

        return new ImageIcon(image);
    }
}
