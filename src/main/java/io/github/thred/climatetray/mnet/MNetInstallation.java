package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetInstallation
{

    STANDING("Standing", ClimateTrayImage.ICON_VERTICAL),
    HANGING("Hanging", ClimateTrayImage.ICON_HORIZONTAL);

    private final String label;
    private final ClimateTrayImage image;

    private MNetInstallation(String label, ClimateTrayImage image)
    {
        this.label = label;
        this.image = image;
    }

    public String getLabel()
    {
        return label;
    }

    public ClimateTrayImage getImage()
    {
        return image;
    }

    public MNetAir translate(MNetAir air)
    {
        if (air == null)
        {
            return null;
        }

        return air.translate(this);
    }

    @Override
    public String toString()
    {
        return getLabel();
    }

    public static String labelOf(MNetInstallation drive)
    {
        return (drive != null) ? drive.getLabel() : null;
    }

}
