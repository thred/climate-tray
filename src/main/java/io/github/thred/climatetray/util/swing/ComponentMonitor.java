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

public class ComponentMonitor implements DocumentListener, ActionListener, ChangeListener, ListDataListener,
    PropertyChangeListener
{

    protected final EventListenerList listenerList = new EventListenerList();

    private String actionCommand = null;
    private boolean monitoring = true;

    public ComponentMonitor()
    {
        super();
    }

    public void addActionListener(ActionListener listener)
    {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener)
    {
        listenerList.remove(ActionListener.class, listener);
    }

    protected void fireActionEvent(ActionEvent e)
    {
        for (ActionListener listener : listenerList.getListeners(ActionListener.class))
        {
            listener.actionPerformed(e);
        }
    }

    public String getActionCommand()
    {
        return actionCommand;
    }

    public void setActionCommand(String actionCommand)
    {
        this.actionCommand = actionCommand;
    }

    public boolean isMonitoring()
    {
        return monitoring;
    }

    public void setMonitoring(boolean monitoring)
    {
        this.monitoring = monitoring;
    }

    public void enableMonitoring()
    {
        setMonitoring(true);
    }

    public void disableMonitoring()
    {
        setMonitoring(false);
    }

    public <TYPE> TYPE monitor(TYPE object)
    {
        if (object == null)
        {
            return null;
        }

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

    public void monitored()
    {
        if (isMonitoring())
        {
            fireActionEvent(new ActionEvent(this, 0, actionCommand));
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e)
    {
        monitored();
    }

    @Override
    public void removeUpdate(DocumentEvent e)
    {
        monitored();
    }

    @Override
    public void changedUpdate(DocumentEvent e)
    {
        monitored();
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        monitored();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        monitored();
    }

    @Override
    public void intervalAdded(ListDataEvent e)
    {
        monitored();
    }

    @Override
    public void intervalRemoved(ListDataEvent e)
    {
        monitored();
    }

    @Override
    public void contentsChanged(ListDataEvent e)
    {
        monitored();
    }

    @Override
    public void propertyChange(PropertyChangeEvent e)
    {
        String name = e.getPropertyName();
        Object oldValue = e.getOldValue();
        Object newValue = e.getNewValue();

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
