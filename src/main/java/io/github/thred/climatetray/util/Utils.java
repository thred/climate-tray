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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Utils
{

    public static final String EMPTY = "";

    public static <TYPE> TYPE ensure(TYPE value, TYPE defaultValue)
    {
        return (value != null) ? value : defaultValue;
    }

    public static boolean isBlank(String s)
    {
        if (s == null)
        {
            return true;
        }

        if (s.length() == 0)
        {
            return true;
        }

        return s.trim().length() == 0;
    }

    public static <TYPE> TYPE[] withNull(TYPE[] values)
    {
        @SuppressWarnings("unchecked")
        TYPE[] result = (TYPE[]) Array.newInstance(values.getClass().getComponentType(), values.length + 1);

        System.arraycopy(values, 0, result, 1, values.length);

        return result;
    }

    public static byte[] toKey(String key) throws UnsupportedEncodingException
    {
        byte[] source = key.getBytes("UTF-8");
        int sourceIndex = 0;
        byte[] target = new byte[8];
        int targetIndex = 0;

        while ((sourceIndex < source.length) || (targetIndex < target.length))
        {
            target[targetIndex % target.length] ^= source[sourceIndex % source.length];

            sourceIndex++;
            targetIndex++;
        }

        return target;
    }

    public static byte[] crypt(String key, String text)
    {
        if (text == null)
        {
            return null;
        }

        try
        {
            Cipher cipher = Cipher.getInstance("Blowfish");

            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(toKey(key), "Blowfish"));

            return cipher.doFinal(text.getBytes("UTF-8"));
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
            | BadPaddingException | UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("Failed to encode text", e);
        }
    }

    public static String decrypt(String key, byte[] bytes)
    {
        if (bytes == null)
        {
            return null;
        }

        try
        {
            Cipher cipher = Cipher.getInstance("Blowfish");

            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(toKey(key), "Blowfish"));

            return new String(cipher.doFinal(bytes), "UTF-8");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
            | BadPaddingException | UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("Failed to encode text", e);
        }
    }

    public static byte[] toByteArray(InputStream in) throws IOException
    {
        byte[] buffer = new byte[4096];

        try (ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            try
            {
                int length;

                while ((length = in.read(buffer)) >= 0)
                {
                    out.write(buffer, 0, length);
                }
            }
            finally
            {
                in.close();
            }

            return out.toByteArray();
        }
    }

    public static String sentence(Object value)
    {
        if (value == null)
        {
            return EMPTY;
        }

        String s = String.valueOf(value).trim();

        if (s.length() <= 0)
        {
            return EMPTY;
        }

        s = Character.toUpperCase(s.charAt(0)) + s.substring(1);

        char last = s.charAt(s.length() - 1);

        if ((!Character.isLetter(last)) && (!Character.isDigit(last)))
        {
            s += ".";
        }

        return s;
    }

    public static String combine(final String delimiter, final Object... values)
    {
        StringBuilder builder = new StringBuilder();

        if (values != null)
        {
            for (Object value : values)
            {
                if (value != null)
                {
                    String s = String.valueOf(value).trim();

                    if (s.length() > 0)
                    {
                        if (builder.length() > 0)
                        {
                            builder.append(delimiter);
                        }

                        builder.append(s);
                    }
                }
            }
        }

        return builder.toString();
    }

    public static String surround(final String bound, final Object value)
    {
        return surround(bound, value, bound);
    }

    public static String surround(final String prefix, final Object value, final String suffix)
    {
        if (value != null)
        {
            String s = String.valueOf(value).trim();

            if (s.length() > 0)
            {
                return prefix + s + suffix;
            }
        }

        return EMPTY;
    }

    public static String pick(final Object... values)
    {
        if ((values != null) && (values.length > 0))
        {
            for (Object value : values)
            {
                if (value != null)
                {
                    String s = String.valueOf(value);

                    if (s.length() > 0)
                    {
                        return s;
                    }
                }
            }
        }

        return EMPTY;
    }

    public static String prefixLine(final String value, final String prefix, final boolean includeFirstLine)
    {
        if (value == null)
        {
            return EMPTY;
        }

        String replacement = "\n" + prefix;
        String result = value.replace("\n", replacement);

        return (includeFirstLine) ? prefix + result : result;
    }

}
