package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetFan
{

    LOW("LOW", "Low", ClimateTrayImage.ICON_FAN1),
    MEDIUM_1("MEDIUM_1", "Medium 1", ClimateTrayImage.ICON_FAN2),
    MEDIUM_2("MEDIUM_2", "Medium 2", ClimateTrayImage.ICON_FAN3),
    HIGH("HIGH", "High", ClimateTrayImage.ICON_FAN4);

    private final String key;
    private final String label;
    private final ClimateTrayImage image;

    private MNetFan(String key, String label, ClimateTrayImage image)
    {
        this.key = key;
        this.label = label;
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

    public ClimateTrayImage getImage()
    {
        return image;
    }
}
