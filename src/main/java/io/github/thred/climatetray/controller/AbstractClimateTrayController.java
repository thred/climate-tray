package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.swing.Monitor;
import io.github.thred.climatetray.util.swing.MonitorListener;

public abstract class AbstractClimateTrayController<MODEL_TYPE, VIEW_TYPE>
{

    protected final Monitor monitor = new Monitor();

    public AbstractClimateTrayController()
    {
        super();
    }

    public void addMonitorListener(MonitorListener listener)
    {
        monitor.addMonitorListener(listener);
    }

    public void removeMonitorListener(MonitorListener listener)
    {
        monitor.removeMonitorListener(listener);
    }

    public <TYPE> TYPE monitor(TYPE object)
    {
        return monitor.monitor(object);
    }

    public abstract VIEW_TYPE getView();

    public abstract void prepare(MODEL_TYPE model);

    public abstract void apply(MODEL_TYPE model);

    public abstract void modified(MessageList messages);

}
