package io.github.thred.climatetray.util.prefs;

import java.util.UUID;

public interface Prefs
{

    Prefs withPrefix(String key);

    boolean existsChild(String key);

    Prefs child(String key);

    boolean removeChild(String key);

    boolean exists(String key);

    Integer getInteger(String key, Integer defaultValue);

    boolean setInteger(String key, Integer value);

    Long getLong(String key, Long defaultValue);

    boolean setLong(String key, Long value);

    Float getFloat(String key, Float defaultValue);

    boolean setFloat(String key, Float value);

    Double getDouble(String key, Double defaultValue);

    boolean setDouble(String key, Double value);

    String getString(String key, String defaultValue);

    boolean setString(String key, String value);

    Boolean getBoolean(String key, Boolean defaultValue);

    boolean setBoolean(String key, Boolean value);

    <TYPE extends Enum<TYPE>> TYPE getEnum(Class<TYPE> type, String key, TYPE defaultValue);

    <TYPE extends Enum<TYPE>> boolean setEnum(String key, TYPE value);

    UUID getUUID(String key, UUID defaultValue);

    boolean setUUID(String key, UUID value);

    boolean remove(String key);

}
