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
import java.util.Iterator;
import java.util.List;

import javax.swing.event.EventListenerList;

public class MessageBuffer implements Iterable<Message>
{
    protected final EventListenerList listenerList = new EventListenerList();
    protected final List<Message> messages = new ArrayList<>();
    protected final boolean delegate;
    protected final int maximumSize;
    
    protected Severity threshold = Severity.DEBUG;

    public MessageBuffer()
    {
        this(false, Integer.MAX_VALUE);
    }

    public MessageBuffer(boolean delegate, int maximumSize)
    {
        super();
        
        this.delegate = delegate;
        this.maximumSize = maximumSize;
    }

    public void addMessageListener(MessageListener listener)
    {
        listenerList.add(MessageListener.class, listener);
    }

    public void removeMessageListener(MessageListener listener)
    {
        listenerList.remove(MessageListener.class, listener);
    }

    protected void fireMessageAdded(Message message)
    {
        for (MessageListener listener : listenerList.getListeners(MessageListener.class))
        {
            listener.messageAdded(message);
        }
    }

    public void debug(String text, Object... args)
    {
        if (isDebugEnabled()) {
            return;
        }
        
        add(Severity.DEBUG, text, args);
    }

    public void debug(String text, Throwable exception, Object... args)
    {
        if (isDebugEnabled()) {
            return;
        }
        
        add(Severity.DEBUG, text, exception, args);
    }

    public boolean isDebugEnabled()
    {
        return threshold.ordinal() > Severity.DEBUG.ordinal();
    }

    public void info(String text, Object... args)
    {
        if (isInfoEnabled()) {
            return;
        }
        
        add(Severity.INFO, text, args);
    }

    public void info(String text, Throwable exception, Object... args)
    {
        if (isInfoEnabled()) {
            return;
        }
        
        add(Severity.INFO, text, exception, args);
    }

    public boolean isInfoEnabled()
    {
        return threshold.ordinal() > Severity.INFO.ordinal();
    }

    public void warn(String text, Object... args)
    {
        if (isWarnEnabled()) {
            return;
        }
        
        add(Severity.WARN, text, args);
    }

    public void warn(String text, Throwable exception, Object... args)
    {
        if (isWarnEnabled()) {
            return;
        }
        
        add(Severity.WARN, text, exception, args);
    }

    public boolean isWarnEnabled()
    {
        return threshold.ordinal() > Severity.WARN.ordinal();
    }

    public void error(String text, Object... args)
    {
        if (isErrorEnabled()) {
            return;
        }
        
        add(Severity.ERROR, text, args);
    }

    public void error(String text, Throwable exception, Object... args)
    {
        if (isErrorEnabled()) {
            return;
        }
        
        add(Severity.ERROR, text, exception, args);
    }

    public boolean isErrorEnabled()
    {
        return threshold.ordinal() > Severity.ERROR.ordinal();
    }

    public void add(Severity severity, String text, Object... args)
    {
        if (isEnabled(severity)) {
            return;
        }
        
        add(new Message(severity, text, args));
    }

    public void add(Severity severity, String text, Throwable exception, Object... args)
    {
        if (isEnabled(severity)) {
            return;
        }
        
        add(new Message(severity, text, exception, args));
    }

    public boolean isEnabled(Severity severity)
    {
        return threshold.ordinal() > severity.ordinal();
    }

    protected void add(Message message)
    {
        if (delegate) {
            message.delegate();
        }
        
        messages.add(message);

        fireMessageAdded(message);
        
        while (messages.size() >= maximumSize) {
            messages.remove(0);
        }
    }

    public void clear()
    {
        messages.clear();
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

    public Message first()
    {
        return (messages.isEmpty()) ? null : messages.get(0);
    }

    public boolean isEmpty()
    {
        return messages.isEmpty();
    }

    @Override
    public Iterator<Message> iterator()
    {
        return messages.iterator();
    }

    public int size()
    {
        return messages.size();
    }

    public void sortBySeverity()
    {
        messages.sort((left, right) -> (right.getSeverity().ordinal() - left.getSeverity().ordinal()));
    }

}
