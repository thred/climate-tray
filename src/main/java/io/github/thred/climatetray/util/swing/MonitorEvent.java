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
