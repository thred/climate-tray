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
package io.github.thred.climatetray.util.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

public class Monitor implements MonitorListener, DocumentListener, ActionListener, ChangeListener, ListDataListener,
    PropertyChangeListener
{

    protected final EventListenerList listenerList = new EventListenerList();

    private int blocks = 0;

    public Monitor()
    {
        super();
    }

    public void addMonitorListener(MonitorListener listener)
    {
        listenerList.add(MonitorListener.class, listener);
    }

    public void removeMonitorListener(MonitorListener listener)
    {
        listenerList.remove(MonitorListener.class, listener);
    }

    protected void fireMonitorEvent(MonitorEvent event)
    {
        for (MonitorListener listener : listenerList.getListeners(MonitorListener.class))
        {
            listener.monitored(event);
        }
    }

    public boolean isMonitoring()
    {
        return blocks <= 0;
    }

    public void block()
    {
        blocks += 1;
    }

    public void unblock()
    {
        if (blocks <= 0)
        {
            throw new IllegalStateException("Not blocked");
        }

        blocks -= 1;
    }

    public <TYPE> TYPE monitor(TYPE object)
    {
        if (object == null)
        {
            return null;
        }

        monitor(object, "addMonitorListener", MonitorListener.class);

        if (object instanceof JTextComponent)
        {
            JTextComponent component = (JTextComponent) object;
            Document document = component.getDocument();

            if (document != null)
            {
                monitor(document);
            }

            component.addPropertyChangeListener(this);
        }

        monitor(object, "addDocumentListener", DocumentListener.class);

        monitor(object, "addActionListener", ActionListener.class);
        monitor(object, "addChangeListener", ChangeListener.class);

        if (object instanceof JList<?>)
        {
            JList<?> list = (JList<?>) object;
            ListModel<?> model = list.getModel();

            if (model != null)
            {
                monitor(model);
            }

            list.addPropertyChangeListener(this);
        }

        monitor(object, "addListDataListener", ListDataListener.class);

        return object;
    }

    public <TYPE> TYPE unmonitor(TYPE object)
    {
        if (object == null)
        {
            return null;
        }

        unmonitor(object, "removeMonitorListener", MonitorListener.class);

        if (object instanceof JTextComponent)
        {
            JTextComponent component = (JTextComponent) object;
            Document document = component.getDocument();

            component.removePropertyChangeListener(this);

            if (document != null)
            {
                unmonitor(document);
            }
        }

        monitor(object, "removeDocumentListener", DocumentListener.class);

        monitor(object, "removeActionListener", ActionListener.class);
        monitor(object, "removeChangeListener", ChangeListener.class);

        if (object instanceof JList<?>)
        {
            JList<?> list = (JList<?>) object;
            ListModel<?> model = list.getModel();

            list.removePropertyChangeListener(this);

            if (model != null)
            {
                unmonitor(model);
            }
        }

        monitor(object, "removeListDataListener", ListDataListener.class);

        return object;
    }

    protected <TYPE> void monitor(TYPE object, String listenerAddMethodName, Class<?> listenerType)
    {
        Class<? extends Object> type = object.getClass();

        try
        {
            type.getMethod(listenerAddMethodName, listenerType).invoke(object, this);
        }
        catch (NoSuchMethodException e)
        {
            // ignore
        }
        catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new IllegalStateException("Failed to add listener", e);
        }
    }

    protected <TYPE> void unmonitor(TYPE object, String listenerRemoveMethodName, Class<?> listenerType)
    {
        Class<? extends Object> type = object.getClass();

        try
        {
            type.getMethod(listenerRemoveMethodName, listenerType).invoke(object, this);
        }
        catch (NoSuchMethodException e)
        {
            // ignore
        }
        catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new IllegalStateException("Failed to remove listener", e);
        }
    }

    @Override
    public void monitored(MonitorEvent event)
    {
        if (isMonitoring())
        {
            fireMonitorEvent(event);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent event)
    {
        monitored(new MonitorEvent(this, event));
    }

    @Override
    public void removeUpdate(DocumentEvent event)
    {
        monitored(new MonitorEvent(this, event));
    }

    @Override
    public void changedUpdate(DocumentEvent event)
    {
        monitored(new MonitorEvent(this, event));
    }

    @Override
    public void stateChanged(ChangeEvent event)
    {
        monitored(new MonitorEvent(this, event));
    }

    @Override
    public void actionPerformed(ActionEvent event)
    {
        monitored(new MonitorEvent(this, event));
    }

    @Override
    public void intervalAdded(ListDataEvent event)
    {
        monitored(new MonitorEvent(this, event));
    }

    @Override
    public void intervalRemoved(ListDataEvent event)
    {
        monitored(new MonitorEvent(this, event));
    }

    @Override
    public void contentsChanged(ListDataEvent event)
    {
        monitored(new MonitorEvent(this, event));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String name = event.getPropertyName();
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if ("model".equals(name))
        {
            unmonitor(oldValue);
            monitor(newValue);
        }

        if ("document".equals(name))
        {
            unmonitor(oldValue);
            monitor(newValue);
        }
    }

}
