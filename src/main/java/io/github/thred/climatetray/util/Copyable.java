package io.github.thred.climatetray.util;

import java.util.Collection;

public interface Copyable<TYPE>
{

    TYPE deepCopy();

    public static <TYPE extends Copyable<TYPE>> TYPE deepCopy(TYPE object)
    {
        return (object != null) ? object.deepCopy() : null;
    }

    @SuppressWarnings("unchecked")
    public static <ELEMENT_TYPE extends Copyable<ELEMENT_TYPE>, COLLECTION_TYPE extends Collection<ELEMENT_TYPE>> COLLECTION_TYPE deepCopy(
        COLLECTION_TYPE list)
    {
        COLLECTION_TYPE results;

        try
        {
            results = (COLLECTION_TYPE) list.getClass().newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            throw new IllegalArgumentException("Failed to copy collection", e);
        }

        return deepCopy(list, results);
    }

    public static <ELEMENT_TYPE extends Copyable<ELEMENT_TYPE>, COLLECTION_TYPE extends Collection<ELEMENT_TYPE>> COLLECTION_TYPE deepCopy(
        COLLECTION_TYPE source, COLLECTION_TYPE target)
    {
        source.forEach((element) -> target.add(element.deepCopy()));

        return target;
    }
}
