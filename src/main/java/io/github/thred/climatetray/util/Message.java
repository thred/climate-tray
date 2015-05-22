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

    public static Message error(String message, Object... args)
    {
        return new Message(Severity.ERROR, message, args);
    }

    public static Message error(String message, Throwable e, Object... args)
    {
        return new Message(Severity.ERROR, message, e, args);
    }

    public static Message warn(String message, Object... args)
    {
        return new Message(Severity.WARN, message, args);
    }

    public static Message warn(String message, Throwable e, Object... args)
    {
        return new Message(Severity.WARN, message, e, args);
    }

    public static Message info(String message, Object... args)
    {
        return new Message(Severity.INFO, message, args);
    }

    public static Message info(String message, Throwable e, Object... args)
    {
        return new Message(Severity.INFO, message, e, args);
    }

    public static Message debug(String message, Object... args)
    {
        return new Message(Severity.DEBUG, message, args);
    }

    public static Message debug(String message, Throwable e, Object... args)
    {
        return new Message(Severity.DEBUG, message, e, args);
    }

    private final Date timestamp = new Date();

    private final Severity severity;
    private final String message;
    private final Throwable exception;

    private String combinedMessage = null;

    public Message(Severity severity, String message, Object... args)
    {
        this(severity, message, (Exception) null, args);
    }

    public Message(Severity severity, String message, Throwable exception, Object... args)
    {
        super();

        this.severity = severity;
        this.message = String.format(message, args);
        this.exception = exception;
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

    public Throwable getException()
    {
        return exception;
    }

    public String getCombinedMessage()
    {
        if (exception == null)
        {
            return message;
        }

        if (combinedMessage != null)
        {
            return combinedMessage;
        }

        return combinedMessage = combine(message, exception);
    }

    public void delegateToSystemStreams()
    {
        switch (severity)
        {
            case ERROR:
            case WARN:
                System.err.println(this);

                if (exception != null)
                {
                    exception.printStackTrace(System.err);
                }
                break;

            default:
                System.out.println(this);

                if (exception != null)
                {
                    exception.printStackTrace(System.out);
                }
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

            return message + " Exception: " + stringWriter.toString();
        }
        catch (IOException e)
        {
            // i don't think, that this may happen
            return message + " [" + exception + "]";
        }
    }
}
