package io.github.thred.climatetray.util.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.event.EventListenerList;

public class ToggleIconGroup<T>
{

    private final EventListenerList listenerList = new EventListenerList();
    private final Map<T, ToggleIcon> icons = new HashMap<>();

    private T value;

    public ToggleIconGroup()
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

    protected void fireActionEvent(ActionEvent event)
    {
        for (ActionListener listener : listenerList.getListeners(ActionListener.class))
        {
            listener.actionPerformed(event);
        }
    }

    public ToggleIcon put(T key, ToggleIcon icon)
    {
        icons.put(key, icon);

        return icon;
    }

    public void setEnabled(boolean enabled)
    {
        icons.values().forEach(icon -> icon.setEnabled(enabled));
    }

    public T getValue()
    {
        return value;
    }

    public void setValue(T value)
    {
        if (this.value != value)
        {
            this.value = value;

            icons.entrySet().forEach(entry -> entry.getValue().setSelected(Objects.equals(value, entry.getKey())));

            ActionEvent event = new ActionEvent(this, -1, null);

            fireActionEvent(event);
        }
    }

}
