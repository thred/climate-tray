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
    ICON,
    ICON_AUTO,
    ICON_AUTO_COOL,
    ICON_AUTO_HEAT,
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
    ICON_OFF,
    ICON_SWING;

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

            case SELECTED:
                backgroundImage = BACKGROUND_SELECTED.getImage(ClimateTrayImageState.NONE, size);
                break;

            case NOT_SELECTED:
                backgroundImage = BACKGROUND.getImage(ClimateTrayImageState.NONE, size);
                break;
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
}
