/*
 * Copyright 2015, 2016 Manfred Hantschel
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

public final class WildcardPattern
{

    public static final char SINGLE_TOKEN = '?';
    public static final char MULTIPLE_TOKEN = '*';

    private final String[] patterns;

    public WildcardPattern(String... patterns)
    {
        super();

        this.patterns = patterns;
    }

    public boolean matches(String s)
    {
        if (s == null)
        {
            return false;
        }

        for (String pattern : patterns)
        {
            if (matches(pattern, 0, s, 0))
            {
                return true;
            }
        }

        return false;
    }

    //CHECKSTYLE:OFF split not possible
    private static boolean matches(final String pattern, final int patternIndex, final String value, final int valueIndex)
    {
        if (!isPattern(pattern))
        {
            return pattern.equals(value);
        }

        int tmpPatternIndex = patternIndex;
        int tmpValueIndex = valueIndex;

        while (tmpPatternIndex < pattern.length())
        {
            if (SINGLE_TOKEN == pattern.charAt(tmpPatternIndex))
            {
                tmpPatternIndex += 1;

                if (tmpValueIndex < value.length())
                {
                    tmpValueIndex += 1;
                }
                else
                {
                    return false;
                }
            }
            else if (MULTIPLE_TOKEN == pattern.charAt(tmpPatternIndex))
            {
                while ((tmpPatternIndex < pattern.length()) && (MULTIPLE_TOKEN == pattern.charAt(tmpPatternIndex)))
                {
                    tmpPatternIndex += 1;
                }

                if (tmpPatternIndex >= pattern.length())
                {
                    return true;
                }

                while (tmpValueIndex < value.length())
                {
                    if (matches(pattern, tmpPatternIndex, value, tmpValueIndex))
                    {
                        return true;
                    }

                    tmpValueIndex += 1;
                }
            }
            else if ((tmpValueIndex < value.length())
                && (pattern.charAt(tmpPatternIndex) == value.charAt(tmpValueIndex)))
            {
                tmpPatternIndex += 1;
                tmpValueIndex += 1;
            }
            else
            {
                return false;
            }
        }

        return ((tmpPatternIndex >= pattern.length()) && (tmpValueIndex >= value.length()));
    }

    //CHECKSTYLE:ON

    private static boolean isPattern(String s)
    {
        if (s == null)
        {
            return false;
        }

        return (s.indexOf(SINGLE_TOKEN) >= 0) || (s.indexOf(MULTIPLE_TOKEN) >= 0);
    }

}