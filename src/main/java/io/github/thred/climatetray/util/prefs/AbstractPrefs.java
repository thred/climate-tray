/*
 * Copyright 2015 Manfred Hantschel
 *
 * This file is part of Climate-Tray.
 *
 * Climate-Tray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * Climate-Tray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Climate-Tray. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package io.github.thred.climatetray.util.prefs;

import java.util.Base64;
import java.util.UUID;

public abstract class AbstractPrefs extends AbstractTypedPrefs
{

    public AbstractPrefs()
    {
        super();
    }

    public AbstractPrefs(String prefix)
    {
        super(prefix);
    }

    @Override
    protected Integer getLocalInteger(String key, Integer defaultValue)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return defaultValue;
        }

        return Integer.valueOf(result);
    }

    @Override
    protected boolean setLocalInteger(String key, Integer value)
    {
        return setLocal(key, String.valueOf(value));
    }

    @Override
    protected Long getLocalLong(String key, Long defaultValue)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return defaultValue;
        }

        return Long.valueOf(result);
    }

    @Override
    protected boolean setLocalLong(String key, Long value)
    {
        return setLocal(key, String.valueOf(value));
    }

    @Override
    protected Float getLocalFloat(String key, Float defaultValue)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return defaultValue;
        }

        return Float.valueOf(result);
    }

    @Override
    protected boolean setLocalFloat(String key, Float value)
    {
        return setLocal(key, String.valueOf(value));
    }

    @Override
    protected Double getLocalDouble(String key, Double defaultValue)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return defaultValue;
        }

        return Double.valueOf(result);
    }

    @Override
    protected boolean setLocalDouble(String key, Double value)
    {
        return setLocal(key, String.valueOf(value));
    }

    @Override
    protected String getLocalString(String key, String defaultValue)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return defaultValue;
        }

        return result;
    }

    @Override
    protected boolean setLocalString(String key, String value)
    {
        return setLocal(key, value);
    }

    @Override
    protected Boolean getLocalBoolean(String key, Boolean defaultValue)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return defaultValue;
        }

        return Boolean.valueOf(result);
    }

    @Override
    protected boolean setLocalBoolean(String key, Boolean value)
    {
        return setLocal(key, String.valueOf(value));
    }

    @Override
    protected <TYPE extends Enum<TYPE>> TYPE getLocalEnum(Class<TYPE> type, String key, TYPE defaultValue)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return defaultValue;
        }

        return Enum.valueOf(type, result);
    }

    @Override
    protected <TYPE extends Enum<?>> boolean setLocalEnum(String key, TYPE value)
    {
        return setLocal(key, String.valueOf(value));
    }

    @Override
    protected UUID getLocalUUID(String key, UUID defaultValue)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return defaultValue;
        }

        return UUID.fromString(result);
    }

    @Override
    protected boolean setLocalUUID(String key, UUID value)
    {
        return setLocal(key, String.valueOf(value));
    }

    @Override
    public byte[] getLocalBytes(String key)
    {
        String result = getLocal(key);

        if (result == null)
        {
            return null;
        }

        return Base64.getDecoder().decode(result);
    }

    @Override
    public boolean setLocalBytes(String key, byte[] bytes)
    {
        return setLocal(key, Base64.getEncoder().encodeToString(bytes));
    }

    protected abstract String getLocal(String key);

    protected abstract boolean setLocal(String key, String value);

}
