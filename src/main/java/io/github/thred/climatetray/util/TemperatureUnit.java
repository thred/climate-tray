package io.github.thred.climatetray.util;

public enum TemperatureUnit
{

    CELSIUS("Degree Celsius", "\u00b0C", 0.5, "%,.1f%s", "0.0 \u00b0C"),
    FAHRENHEIT("Degree Fahrenheit", "\u00b0F", 1.0, "%,.0f%s", "0 \u00b0F"),
    KELVIN("Degree Kelvin", "\u00b0K", 0.5, "%,.1f%s", "0.0 \u00b0K");

    private final String label;
    private final String symbol;
    private final String description;
    private final double step;
    private final String format;
    private final String numberFormat;

    private TemperatureUnit(String label, String symbol, double step, String format, String numberFormat)
    {
        this.label = label;
        this.symbol = symbol;
        this.step = step;
        this.format = format;
        this.numberFormat = numberFormat;

        description = String.format("%s (%s)", label, symbol);
    }

    public String getLabel()
    {
        return label;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public String getDescription()
    {
        return description;
    }

    public double getStep()
    {
        return step;
    }

    public String getFormat()
    {
        return format;
    }

    public String format(double degreeCelsius)
    {
        return String.format(format, convertFromCelsius(degreeCelsius), symbol);
    }

    public String getNumberFormat()
    {
        return numberFormat;
    }

    public double convertFromCelsius(double degreeCelsius)
    {
        switch (this)
        {
            case CELSIUS:
                return degreeCelsius;

            case FAHRENHEIT:
                return (degreeCelsius * 1.8) + 32;

            case KELVIN:
                return degreeCelsius + 273.15;

            default:
                throw new UnsupportedOperationException(this + " not supported");
        }
    }

    public double convertToCelsius(double degrees)
    {
        switch (this)
        {
            case CELSIUS:
                return degrees;

            case FAHRENHEIT:
                return ((degrees - 32) * 5) / 9;

            case KELVIN:
                return degrees - 273.15;

            default:
                throw new UnsupportedOperationException(this + " not supported");
        }
    }

    @Override
    public String toString()
    {
        return getDescription();
    }

}
