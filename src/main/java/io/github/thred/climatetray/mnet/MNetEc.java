package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.ClimateTray.*;

public enum MNetEc
{

    NONE(null, "Not Used"),
    EC_1("1", "EC #1"),
    EC_2("2", "EC #2"),
    EC_3("3", "EC #3");

    public static MNetEc valueOfKey(String key)
    {
        if ((key == null) || ("*".equals(key)))
        {
            return null;
        }

        for (MNetEc ec : values())
        {
            if (key.equals(ec.getKey()))
            {
                return ec;
            }
        }

        LOG.error("Failed to parse EC key \"%s\"", key);

        return null;
    }

    private final String key;
    private final String label;

    private MNetEc(String key, String label)
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

    public static String labelOf(MNetEc mode)
    {
        return (mode != null) ? mode.getLabel() : null;
    }

}
