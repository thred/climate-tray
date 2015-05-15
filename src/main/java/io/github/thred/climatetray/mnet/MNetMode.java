package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetMode
{

    OFF(null, "Off", "Off", ClimateTrayImage.ICON_OFF, ClimateTrayImage.BACKGROUND_OFF, false, false, false),
    FAN("FAN", "Fan", "Blowing", ClimateTrayImage.ICON_FAN, ClimateTrayImage.BACKGROUND_FAN1, false, true, true),
    COOL("COOL", "Cool", "Cooling", ClimateTrayImage.ICON_COOL, ClimateTrayImage.BACKGROUND_COOL, true, true, true),
    HEAT("HEAT", "Heat", "Heating", ClimateTrayImage.ICON_HEAT, ClimateTrayImage.BACKGROUND_HEAT, true, true, true),
    AUTO("AUTO", "Automatic", "Auto", ClimateTrayImage.ICON_AUTO, ClimateTrayImage.BACKGROUND_AUTO, true, true, true),
    DRY("DRY", "Dry", "Drying", ClimateTrayImage.ICON_DRY, ClimateTrayImage.BACKGROUND_DRY, true, true, true);

    private final String key;
    private final String label;
    private final String description;
    private final ClimateTrayImage image;
    private final ClimateTrayImage backgroundImage;
    private boolean temperatureEnabled;
    private boolean fanEnabled;
    private boolean airEnabled;

    private MNetMode(String key, String label, String description, ClimateTrayImage image,
        ClimateTrayImage backgroundImage, boolean temperatureEnabled, boolean fanEnabled, boolean airEnabled)
    {
        this.key = key;
        this.label = label;
        this.description = description;
        this.image = image;
        this.backgroundImage = backgroundImage;
        this.temperatureEnabled = temperatureEnabled;
        this.fanEnabled = fanEnabled;
        this.airEnabled = airEnabled;
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

    public boolean isTemperatureEnabled()
    {
        return temperatureEnabled;
    }

    public void setTemperatureEnabled(boolean temperatureEnabled)
    {
        this.temperatureEnabled = temperatureEnabled;
    }

    public boolean isFanEnabled()
    {
        return fanEnabled;
    }

    public void setFanEnabled(boolean fanEnabled)
    {
        this.fanEnabled = fanEnabled;
    }

    public boolean isAirEnabled()
    {
        return airEnabled;
    }

    public void setAirEnabled(boolean airEnabled)
    {
        this.airEnabled = airEnabled;
    }

}
