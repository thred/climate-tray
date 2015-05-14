package io.github.thred.climatetray.util.prefs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MapPrefs extends AbstractPrefs
{

    private final Map<String, String> values;
    private final Map<String, MapPrefs> childs;

    public MapPrefs()
    {
        this(new HashMap<>(), new HashMap<>(), null);
    }

    public MapPrefs(String prefix)
    {
        this(new HashMap<>(), new HashMap<>(), prefix);
    }

    protected MapPrefs(Map<String, String> values, Map<String, MapPrefs> childs, String prefix)
    {
        super(prefix);

        this.values = values;
        this.childs = childs;
    }

    @Override
    public Prefs withPrefix(String key)
    {
        return new MapPrefs(values, childs, prefix(prefix, key));
    }

    @Override
    protected boolean containsLocalChild(String key)
    {
        return childs.containsKey(key);
    }

    @Override
    protected Prefs localChild(String key)
    {
        MapPrefs child = childs.get(key);

        if (child == null)
        {
            child = new MapPrefs();
        }

        return child;
    }

    @Override
    protected boolean removeLocalChild(String key)
    {
        return childs.remove(key) != null;
    }

    @Override
    protected boolean containsLocal(String key)
    {
        return values.containsKey(key);
    }

    @Override
    protected String getLocal(String key)
    {
        return values.get(key);
    }

    @Override
    protected boolean setLocal(String key, String value)
    {
        String existingValue = getLocal(key);

        if (Objects.equals(value, existingValue))
        {
            return false;
        }

        values.put(key, value);

        return true;
    }

    @Override
    protected boolean removeLocal(String key)
    {
        values.remove(key);

        return true;
    }

}
