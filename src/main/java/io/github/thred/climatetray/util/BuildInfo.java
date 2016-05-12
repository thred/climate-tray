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

import io.github.thred.climatetray.ClimateTray;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Properties;

public class BuildInfo
{

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static BuildInfo createDefault()
    {
        try
        {
            return BuildInfo.create(ClimateTray.class.getResourceAsStream("/VERSION"));
        }
        catch (IOException e)
        {
            ClimateTray.LOG.error("Failed to load local build info", e);

            return null;
        }
    }

    public static BuildInfo create(InputStream in) throws IOException
    {
        Properties properties = new Properties();

        properties.load(in);

        return create(properties);
    }

    public static BuildInfo create(Properties properties)
    {
        String version = properties.getProperty("build.version");
        LocalDateTime timestamp = null;

        try
        {
            timestamp = LocalDateTime.parse(properties.getProperty("build.timestamp"), FORMATTER);
        }
        catch (DateTimeParseException e)
        {
            ClimateTray.LOG.warn("Failed ot parse build timestamp", e);
        }

        return new BuildInfo(version, timestamp);
    }

    private final String version;
    private final LocalDateTime timestamp;

    public BuildInfo(String version, LocalDateTime timestamp)
    {
        super();
        this.version = version;
        this.timestamp = timestamp;
    }

    public String getVersion()
    {
        return version;
    }

    public LocalDateTime getTimestamp()
    {
        return timestamp;
    }

    public Object getTimestampString()
    {
        return (timestamp != null) ? FORMATTER.format(timestamp) : null;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = (prime * result) + ((timestamp == null) ? 0 : timestamp.hashCode());
        result = (prime * result) + ((version == null) ? 0 : version.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }

        if (obj == null)
        {
            return false;
        }

        if (getClass() != obj.getClass())
        {
            return false;
        }

        BuildInfo other = (BuildInfo) obj;

        if (timestamp == null)
        {
            if (other.timestamp != null)
            {
                return false;
            }
        }
        else if (!timestamp.equals(other.timestamp))
        {
            return false;
        }

        if (version == null)
        {
            if (other.version != null)
            {
                return false;
            }
        }
        else if (!version.equals(other.version))
        {
            return false;
        }

        return true;
    }

    @Override
    public String toString()
    {
        return String.format("%s (%s)", getVersion(), getTimestampString());
    }

}
