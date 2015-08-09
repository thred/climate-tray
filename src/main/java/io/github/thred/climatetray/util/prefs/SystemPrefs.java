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
import java.util.Objects;
import java.util.UUID;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class SystemPrefs extends AbstractTypedPrefs
{

    private static SystemPrefs defaultPrefs = null;

    public static SystemPrefs getDefault()
    {
        return defaultPrefs;
    }

    public static void setDefault(Class<?> type)
    {
        defaultPrefs = new SystemPrefs(Preferences.userNodeForPackage(type), null);
    }

    public static SystemPrefs get(Class<?> type)
    {
        return new SystemPrefs(Preferences.userNodeForPackage(type), null);
    }

    private final Preferences prefs;

    protected SystemPrefs(Preferences prefs, String prefix)
    {
        super(prefix);

        this.prefs = prefs;
    }

    @Override
    public Prefs withPrefix(String key)
    {
        return new SystemPrefs(prefs, prefix(prefix, key));
    }

    @Override
    protected boolean containsLocalChild(String key)
    {
        try
        {
            return prefs.nodeExists(key);
        }
        catch (BackingStoreException e)
        {
            return false;
        }
    }

    @Override
    protected Prefs localChild(String key)
    {
        return new SystemPrefs(prefs.node(key), null);
    }

    @Override
    protected boolean removeLocalChild(String key)
    {
        try
        {
            prefs.node(key).removeNode();
        }
        catch (BackingStoreException e)
        {
            return false;
        }

        return true;
    }

    @Override
    protected boolean containsLocal(String key)
    {
        return prefs.get(key, null) != null;
    }

    @Override
    protected Integer getLocalInteger(String key, Integer defaultValue)
    {
        return prefs.getInt(key, (defaultValue != null) ? defaultValue : 0);
    }

    @Override
    protected boolean setLocalInteger(String key, Integer value)
    {
        if ((Objects.equals(prefs.getInt(key, 0), prefs.getInt(key, 1)))
            && (Objects.equals(prefs.getInt(key, 0), value)))
        {
            return false;
        }

        prefs.putInt(key, value);

        return true;
    }

    @Override
    protected Long getLocalLong(String key, Long defaultValue)
    {
        return prefs.getLong(key, (defaultValue != null) ? defaultValue : 0);
    }

    @Override
    protected boolean setLocalLong(String key, Long value)
    {
        if ((Objects.equals(prefs.getLong(key, 0), prefs.getLong(key, 1)))
            && (Objects.equals(prefs.getLong(key, 0), value)))
        {
            return false;
        }

        prefs.putLong(key, value);

        return true;
    }

    @Override
    protected Float getLocalFloat(String key, Float defaultValue)
    {
        return prefs.getFloat(key, (defaultValue != null) ? defaultValue : 0);
    }

    @Override
    protected boolean setLocalFloat(String key, Float value)
    {
        if ((Objects.equals(prefs.getFloat(key, 0), prefs.getFloat(key, 1)))
            && (Objects.equals(prefs.getFloat(key, 0), value)))
        {
            return false;
        }

        prefs.putFloat(key, value);

        return true;
    }

    @Override
    protected Double getLocalDouble(String key, Double defaultValue)
    {
        return prefs.getDouble(key, (defaultValue != null) ? defaultValue : 0);
    }

    @Override
    protected boolean setLocalDouble(String key, Double value)
    {
        if ((Objects.equals(prefs.getDouble(key, 0), prefs.getDouble(key, 1)))
            && (Objects.equals(prefs.getDouble(key, 0), value)))
        {
            return false;
        }

        prefs.putDouble(key, value);

        return true;
    }

    @Override
    protected String getLocalString(String key, String defaultValue)
    {
        return prefs.get(key, defaultValue);
    }

    @Override
    protected boolean setLocalString(String key, String value)
    {
        if (Objects.equals(prefs.get(key, null), value))
        {
            return false;
        }

        prefs.put(key, value);

        return true;
    }

    @Override
    protected Boolean getLocalBoolean(String key, Boolean defaultValue)
    {
        return prefs.getBoolean(key, (defaultValue != null) ? defaultValue : false);
    }

    @Override
    protected boolean setLocalBoolean(String key, Boolean value)
    {
        if ((Objects.equals(prefs.getBoolean(key, true), prefs.getBoolean(key, false)))
            && (Objects.equals(prefs.getBoolean(key, false), value)))
        {
            return false;
        }

        prefs.putBoolean(key, value);

        return true;
    }

    @Override
    protected <TYPE extends Enum<TYPE>> TYPE getLocalEnum(Class<TYPE> type, String key, TYPE defaultValue)
    {
        String result = getLocalString(key, (defaultValue != null) ? defaultValue.toString() : null);

        return (result != null) ? Enum.valueOf(type, result) : defaultValue;
    }

    @Override
    protected <TYPE extends Enum<?>> boolean setLocalEnum(String key, TYPE value)
    {
        return setLocalString(key, value.name());
    }

    @Override
    protected UUID getLocalUUID(String key, UUID defaultValue)
    {
        String result = getLocalString(key, (defaultValue != null) ? defaultValue.toString() : null);

        return (result != null) ? UUID.fromString(result) : defaultValue;
    }

    @Override
    protected boolean setLocalUUID(String key, UUID value)
    {
        return setLocalString(key, value.toString());
    }

    @Override
    protected byte[] getLocalBytes(String key)
    {
        String result = getLocalString(key, null);

        return (result != null) ? Base64.getDecoder().decode(result) : null;
    }

    @Override
    protected boolean setLocalBytes(String key, byte[] value)
    {
        return setLocalString(key, Base64.getEncoder().encodeToString(value));
    }

    @Override
    protected boolean removeLocal(String key)
    {
        prefs.remove(key);

        return true;
    }

    //
    //    public byte[] getByteArray(String key, byte[] defaultValue)
    //    {
    //        return prefs.getByteArray(key, defaultValue);
    //    }
    //
    //    public boolean setByteArray(String key, byte[] value)
    //    {
    //        if ((exists(key)) && (Objects.equals(prefs.get(key, null), value)))
    //        {
    //            return false;
    //        }
    //
    //        prefs.putByteArray(key, value);
    //
    //        return true;
    //    }

    //    @SuppressWarnings("unchecked")
    //    public <TYPE extends Serializable> TYPE getObject(Class<TYPE> type, String key, TYPE defaultValue)
    //    {
    //        byte[] bytes = prefs.getByteArray(key, null);
    //
    //        if (bytes == null)
    //        {
    //            return defaultValue;
    //        }
    //
    //        try
    //        {
    //            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
    //
    //            try
    //            {
    //                return (TYPE) in.readObject();
    //            }
    //            catch (ClassNotFoundException e)
    //            {
    //                return defaultValue;
    //            }
    //            finally
    //            {
    //                in.close();
    //            }
    //        }
    //        catch (IOException e)
    //        {
    //            return defaultValue;
    //        }
    //    }
    //
    //    public <TYPE extends Serializable> boolean setObject(Class<TYPE> type, String key, TYPE value)
    //    {
    //        if (value == null)
    //        {
    //            if (exists(key))
    //            {
    //                prefs.remove(key);
    //
    //                return true;
    //            }
    //
    //            return false;
    //        }
    //
    //        try
    //        {
    //            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    //
    //            try
    //            {
    //                ObjectOutputStream out = new ObjectOutputStream(byteStream);
    //
    //                out.writeObject(value);
    //            }
    //            finally
    //            {
    //                byteStream.close();
    //            }
    //
    //            prefs.putByteArray(key, byteStream.toByteArray());
    //
    //            return true;
    //        }
    //        catch (IOException e)
    //        {
    //            throw new IllegalArgumentException("Failed to store value of type " + type + " with key " + key);
    //        }
    //    }

}
