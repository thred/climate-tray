package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetDrive
{

    NO_CHANGE(null, "Do not change", null, ClimateTrayImage.ICON_JOKER, null),
    ON("ON", "On", "turn on", ClimateTrayImage.ICON_ON, null),
    OFF("OFF", "Off", "turn off", ClimateTrayImage.ICON_OFF, ClimateTrayImage.BACKGROUND_OFF);

    public static MNetDrive valueOfKey(String key)
    {
        if ((key == null) || ("*".equals(key)))
        {
            return null;
        }

        for (MNetDrive ec : values())
        {
            if (key.equals(ec.getKey()))
            {
                return ec;
            }
        }

        LOG.error("Failed to parse drive key \"%s\"", key);

        return null;
    }

    private final String key;
    private final String label;
    private final String description;
    private final ClimateTrayImage image;
    private final ClimateTrayImage backgroundImage;

    private MNetDrive(String key, String label, String description, ClimateTrayImage image,
        ClimateTrayImage backgroundImage)
    {
        this.key = key;
        this.label = label;
        this.description = description;
        this.image = image;
        this.backgroundImage = backgroundImage;
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

    public ClimateTrayImage getBackgroundImage()
    {
        return backgroundImage;
    }

    @Override
    public String toString()
    {
        return getLabel();
    }

    public static String labelOf(MNetDrive drive)
    {
        return (drive != null) ? drive.getLabel() : null;
    }

    public static String descriptionOf(MNetDrive drive)
    {
        return (drive != null) ? drive.getDescription() : null;
    }
}
