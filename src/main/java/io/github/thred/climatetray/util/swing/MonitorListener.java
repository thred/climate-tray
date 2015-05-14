package io.github.thred.climatetray.util.swing;

import java.util.EventListener;

public interface MonitorListener extends EventListener
{

    void monitored(MonitorEvent event);

}
