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

import java.util.ArrayList;
import java.util.Collection;

public class MessageList extends ArrayList<Message>
{

    private static final long serialVersionUID = 2788447149905230681L;

    public MessageList()
    {
        super();
    }

    public MessageList(Collection<? extends Message> c)
    {
        super(c);
    }

    public void addInfo(String message, Object... args)
    {
        add(Severity.INFO, message, args);
    }

    public void addWarning(String message, Object... args)
    {
        add(Severity.WARNING, message, args);
    }

    public void addError(String message, Object... args)
    {
        add(Severity.ERROR, message, args);
    }

    public void add(Severity severity, String message, Object... args)
    {
        add(new Message(severity, message, args));
    }

    public boolean containsAtLeast(Severity severity)
    {
        for (Message message : this)
        {
            if (message.getSeverity().ordinal() >= severity.ordinal())
            {
                return true;
            }
        }

        return false;
    }

    public void sortBySeverity()
    {
        sort((left, right) -> (right.getSeverity().ordinal() - left.getSeverity().ordinal()));
    }
    
}
