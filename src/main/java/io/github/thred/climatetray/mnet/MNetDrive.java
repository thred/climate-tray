package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTray;

public enum MNetDrive
{

    OFF("OFF", "Off"),
    ON("ON", "On");

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

        ClimateTray.LOG.error("Failed to parse drive key \"%s\"", key);

        return null;
    }

    private final String key;
    private final String label;

    private MNetDrive(String key, String label)
    {
        this.key = key;
        this.label = label;
    }

    public String getKey()
    {
        return key;
    }

    public String getLabel()
    {
        return label;
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
}
