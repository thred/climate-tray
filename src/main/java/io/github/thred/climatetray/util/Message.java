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

public class Message
{

    private final Severity severity;
    private final String message;

    public Message(Severity severity, String message, Object... args)
    {
        super();

        this.severity = severity;
        this.message = String.format(message, args);
    }

    public Severity getSeverity()
    {
        return severity;
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return String.format("%s: %s", severity, message);
    }
}
