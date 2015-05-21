package io.github.thred.climatetray.mnet;

public enum MNetEc
{

    NONE(null, "Not Used"),
    EC_1("1", "EC #1"),
    EC_2("2", "EC #2"),
    EC_3("3", "EC #3");

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
}
