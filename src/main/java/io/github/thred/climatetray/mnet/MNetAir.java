package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetAir
{

    POSITION_1("POSITION_1", "Position 1 (Horizontal)", ClimateTrayImage.ICON_DIR1),
    POSITION_2("POSITION_2", "Position 2", ClimateTrayImage.ICON_DIR2),
    POSITION_3("POSITION_3", "Position 3", ClimateTrayImage.ICON_DIR3),
    POSITION_4("POSITION_4", "Position 4", ClimateTrayImage.ICON_DIR4),
    SWING("SWING", "Swing", ClimateTrayImage.ICON_SWING);

    private final String key;
    private final String label;
    private final ClimateTrayImage image;

    private MNetAir(String key, String label, ClimateTrayImage image)
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
