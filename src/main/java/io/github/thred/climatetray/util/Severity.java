package io.github.thred.climatetray.util;

import io.github.thred.climatetray.ClimateTrayImage;

public enum Severity
{

    INFO(ClimateTrayImage.ICON_INFO),

    WARNING(ClimateTrayImage.ICON_WARNING),

    ERROR(ClimateTrayImage.ICON_ERROR);

    private final ClimateTrayImage image;

    private Severity(ClimateTrayImage image)
    {
        this.image = image;
    }

    public ClimateTrayImage getImage()
    {
        return image;
    }

}
