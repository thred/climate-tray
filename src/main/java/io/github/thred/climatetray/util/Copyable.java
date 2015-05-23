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
