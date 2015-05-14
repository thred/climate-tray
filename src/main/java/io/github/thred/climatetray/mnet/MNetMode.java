package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImage;

public enum MNetMode
{

    OFF(null, "Off", ClimateTrayImage.ICON_OFF, false, false, false),
    FAN("FAN", "Fan", ClimateTrayImage.ICON_FAN, false, true, true),
    AUTO_COOL("AUTO_COOL", "Cool (automatically)", ClimateTrayImage.ICON_AUTO_COOL, true, true, true),
    AUTO_HEAT("AUTO_HEAT", "Heat (automatically)", ClimateTrayImage.ICON_AUTO_HEAT, true, true, true),
    COOL("COOL", "Cool", ClimateTrayImage.ICON_COOL, false, true, true),
    HEAT("HEAT", "Heat", ClimateTrayImage.ICON_HEAT, false, true, true),
    DRY("DRY", "Dry", ClimateTrayImage.ICON_DRY, true, true, true);

    private final String key;
    private final String label;
    private final ClimateTrayImage image;
    private boolean temperatureEnabled;
    private boolean fanEnabled;
    private boolean airEnabled;

    private MNetMode(String key, String label, ClimateTrayImage image, boolean temperatureEnabled, boolean fanEnabled,
        boolean airEnabled)
    {
        this.key = key;
        this.label = label;
        this.image = image;
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

    public ClimateTrayImage getImage()
    {
        return image;
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
