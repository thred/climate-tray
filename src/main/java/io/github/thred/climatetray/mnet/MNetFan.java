package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetFan
{

    LOW("LOW", "Very Low", "very low fan speed", ClimateTrayImage.ICON_FAN1),
    MEDIUM_1("MEDIUM_1", "Low", "low fan speed", ClimateTrayImage.ICON_FAN2),
    MEDIUM_2("MEDIUM_2", "High", "high fan speed", ClimateTrayImage.ICON_FAN3),
    HIGH("HIGH", "Very High", "very high fan speed", ClimateTrayImage.ICON_FAN4);

    private final String key;
    private final String label;
    private final String description;
    private final ClimateTrayImage image;

    private MNetFan(String key, String label, String description, ClimateTrayImage image)
    {
        this.key = key;
        this.label = label;
        this.description = description;
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

    public String getDescription()
    {
        return description;
    }

    public ClimateTrayImage getImage()
    {
        return image;
    }
}
