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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

public class Message
{

    private final Date timestamp = new Date();

    private final Severity severity;
    private final String message;

    public Message(Severity severity, String message, Object... args)
    {
        super();

        this.severity = severity;
        this.message = String.format(message, args);
    }

    public Message(Severity severity, String message, Throwable exception, Object... args)
    {
        this(severity, combine(String.format(message, args), exception));
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public Severity getSeverity()
    {
        return severity;
    }

    public String getMessage()
    {
        return message;
    }

    public void delegate()
    {
        switch (severity)
        {
            case ERROR:
            case WARN:
                System.err.println(this);
                break;

            default:
                System.out.println(this);
                break;
        }
    }

    @Override
    public String toString()
    {
        return String.format("%1$tH:%1$tM:%1$tS.%1$tL %2$5s: %3$s", timestamp, severity, message);
    }

    private static String combine(String message, Throwable exception)
    {
        if (exception == null)
        {
            return message;
        }

        try (StringWriter stringWriter = new StringWriter())
        {
            try (PrintWriter printWriter = new PrintWriter(stringWriter, true))
            {
                exception.printStackTrace(printWriter);
            }

            return message + "\n\t" + stringWriter.toString().replace("\n", "\n\t");
        }
        catch (IOException e)
        {
            // i don't think, that this may happen
            return message + " [" + exception + "]";
        }
    }
}
