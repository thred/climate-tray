package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImageState;

import java.awt.Graphics2D;
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

        g.drawImage(mode.getImage().getImage(state, size), 0, 0, null);

        return new ImageIcon(image);
    }
}
