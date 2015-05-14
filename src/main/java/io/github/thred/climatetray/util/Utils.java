package io.github.thred.climatetray.util;

import java.lang.reflect.Array;

public class Utils
{

    public static <TYPE> TYPE ensure(TYPE value, TYPE defaultValue)
    {
        return (value != null) ? value : defaultValue;
    }

    public static <TYPE> TYPE[] withNull(TYPE[] values)
    {
        @SuppressWarnings("unchecked")
        TYPE[] result = (TYPE[]) Array.newInstance(values.getClass().getComponentType(), values.length + 1);

        System.arraycopy(values, 0, result, 1, values.length);

        return result;
    }
}
