package io.github.thred.climatetray.util.swing;

public class MonitorEvent
{

    private final Monitor monitor;
    private final Object source;

    public MonitorEvent(Monitor monitor, Object source)
    {
        super();

        this.monitor = monitor;
        this.source = source;
    }

    public Monitor getMonitor()
    {
        return monitor;
    }

    public Object getSource()
    {
        return source;
    }

}
