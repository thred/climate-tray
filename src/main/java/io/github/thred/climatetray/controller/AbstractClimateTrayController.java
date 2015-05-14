package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.Severity;
import io.github.thred.climatetray.util.swing.ComponentMonitor;

import javax.swing.SwingUtilities;

public abstract class AbstractClimateTrayController<MODEL_TYPE, VIEW_TYPE>
{

    protected final MessageList messages = new MessageList();
    protected final ComponentMonitor monitor = new ComponentMonitor();

    private VIEW_TYPE view;
    private MODEL_TYPE model;

    public AbstractClimateTrayController()
    {
        super();

        monitor.addActionListener((e) -> check());
    }

    protected abstract VIEW_TYPE createView();

    public VIEW_TYPE getView()
    {
        if (view == null)
        {
            view = createView();
        }

        return view;
    }

    public void refreshView()
    {
        // intentionally left blank
    }

    public MODEL_TYPE getModel()
    {
        return model;
    }

    public ComponentMonitor getMonitor()
    {
        return monitor;
    }

    public <TYPE> TYPE monitor(TYPE component)
    {
        return monitor.monitor(component);
    }

    public <TYPE> TYPE unmonitor(TYPE component)
    {
        return monitor.unmonitor(component);
    }

    public final void prepare(MODEL_TYPE model)
    {
        this.model = model;

        monitor.disableMonitoring();

        try
        {
            localPrepare(model);
        }
        finally
        {
            SwingUtilities.invokeLater(() -> {
                monitor.enableMonitoring();
                check();
            });
        }
    }

    protected abstract void localPrepare(MODEL_TYPE model);

    public final MODEL_TYPE apply()
    {
        localApply(model);

        return model;
    }

    protected abstract void localApply(MODEL_TYPE model);

    public final MessageList check()
    {
        MessageList messages = new MessageList();

        localCheck(messages);

        this.messages.clear();
        this.messages.addAll(messages);
        this.messages.sortBySeverity();

        boolean valid = isValid(this.messages);

        checked(valid, this.messages);

        return this.messages;
    }

    protected void checked(boolean valid, MessageList messages)
    {
        // intentionally left blank
    }

    protected abstract void localCheck(MessageList messages);

    public boolean isValid(MessageList messages)
    {
        return !messages.containsAtLeast(Severity.ERROR);
    }

    public MessageList getMessages()
    {
        return messages;
    }

}
