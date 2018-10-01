/*
 * Copyright 2015 - 2018 Manfred Hantschel
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
package io.github.thred.climatetray.util.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.event.EventListenerList;

import io.github.thred.climatetray.util.Severity;

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
            listener.messageAdded(this, message);
        }
    }

    protected void fireMessagesCleared()
    {
        for (MessageListener listener : listenerList.getListeners(MessageListener.class))
        {
            listener.messagesCleared(this);
        }
    }

    protected void fireMessageRemoved(Message message)
    {
        for (MessageListener listener : listenerList.getListeners(MessageListener.class))
        {
            listener.messageRemoved(this, message);
        }
    }

    public void error(String text, Object... args)
    {
        if (!isErrorEnabled())
        {
            return;
        }

        add(Severity.ERROR, text, args);
    }

    public void error(String text, Throwable exception, Object... args)
    {
        if (!isErrorEnabled())
        {
            return;
        }

        add(Severity.ERROR, text, exception, args);
    }

    public boolean isErrorEnabled()
    {
        return isEnabled(Severity.ERROR);
    }

    public void warn(String text, Object... args)
    {
        if (!isWarnEnabled())
        {
            return;
        }

        add(Severity.WARN, text, args);
    }

    public void warn(String text, Throwable exception, Object... args)
    {
        if (!isWarnEnabled())
        {
            return;
        }

        add(Severity.WARN, text, exception, args);
    }

    public boolean isWarnEnabled()
    {
        return isEnabled(Severity.WARN);
    }

    public void info(String text, Object... args)
    {
        if (!isInfoEnabled())
        {
            return;
        }

        add(Severity.INFO, text, args);
    }

    public void info(String text, Throwable exception, Object... args)
    {
        if (!isInfoEnabled())
        {
            return;
        }

        add(Severity.INFO, text, exception, args);
    }

    public boolean isInfoEnabled()
    {
        return isEnabled(Severity.INFO);
    }

    public void debug(String text, Object... args)
    {
        if (!isDebugEnabled())
        {
            return;
        }

        add(Severity.DEBUG, text, args);
    }

    public void debug(String text, Throwable exception, Object... args)
    {
        if (!isDebugEnabled())
        {
            return;
        }

        add(Severity.DEBUG, text, exception, args);
    }

    public boolean isDebugEnabled()
    {
        return isEnabled(Severity.DEBUG);
    }

    public void add(Severity severity, String text, Object... args)
    {
        if (!isEnabled(severity))
        {
            return;
        }

        add(new Message(severity, text, args));
    }

    public void add(Severity severity, String text, Throwable exception, Object... args)
    {
        if (!isEnabled(severity))
        {
            return;
        }

        add(new Message(severity, text, exception, args));
    }

    public boolean isEnabled(Severity severity)
    {
        return severity.isCoveredBy(threshold);
    }

    public Message add(Message message)
    {
        if (isEnabled(message.getSeverity()))
        {
            if (delegate)
            {
                message.delegateToSystemStreams();
            }

            messages.add(message);

            fireMessageAdded(message);

            while (messages.size() >= maximumSize)
            {
                fireMessageRemoved(messages.remove(0));
            }
        }

        return message;
    }

    public void clear()
    {
        messages.clear();

        fireMessagesCleared();
    }

    public boolean containsAtLeast(Severity severity)
    {
        for (Message message : this)
        {
            if (message.getSeverity().isCoveredBy(severity))
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

    public Message worst()
    {
        return messages.stream().sorted().findFirst().orElse(null);
    }

    public Message first(Severity severity)
    {
        return messages.stream().filter(message -> message.getSeverity() == severity).findFirst().orElse(null);
    }

    public boolean isEmpty()
    {
        return messages.isEmpty();
    }

    @Override
    public Iterator<Message> iterator()
    {
        return Collections.unmodifiableCollection(messages).iterator();
    }

    public int size()
    {
        return messages.size();
    }

    public void sortBySeverity()
    {
        Collections.sort(messages);
    }

}
