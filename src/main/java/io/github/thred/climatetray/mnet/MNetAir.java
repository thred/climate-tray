package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetAir
{

    POSITION_1("POSITION_1", "Horizontal", "fins horizontal", ClimateTrayImage.ICON_DIR1),
    POSITION_2("POSITION_2", "Slightly Inclined", "fins slightly inclined", ClimateTrayImage.ICON_DIR2),
    POSITION_3("POSITION_3", "Stronly Inclined", "fins stronly inclined", ClimateTrayImage.ICON_DIR3),
    POSITION_4("POSITION_4", "Vertical", "fins vertical", ClimateTrayImage.ICON_DIR4),
    SWING("SWING", "Swing", "fins swining", ClimateTrayImage.ICON_SWING);

    private final String key;
    private final String label;
    private final String description;
    private final ClimateTrayImage image;

    private MNetAir(String key, String label, String description, ClimateTrayImage image)
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
