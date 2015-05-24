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
package io.github.thred.climatetray.ui;

import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.Monitor;
import io.github.thred.climatetray.util.swing.MonitorListener;

public abstract class AbstractClimateTrayController<MODEL_TYPE, VIEW_TYPE>
{

    protected final Monitor monitor = new Monitor();

    private VIEW_TYPE view = null;

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

    protected abstract VIEW_TYPE createView();

    public final VIEW_TYPE getView()
    {
        if (view == null)
        {
            view = createView();
        }

        return view;
    }

    public void prepareWith(MODEL_TYPE model)
    {
        refreshWith(model);
    }

    public abstract void refreshWith(MODEL_TYPE model);

    public abstract void modified(MessageBuffer messageBuffer);

    public abstract void applyTo(MODEL_TYPE model);

    public abstract void dismiss(MODEL_TYPE model);

}
