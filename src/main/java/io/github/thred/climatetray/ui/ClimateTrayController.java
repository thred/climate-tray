package io.github.thred.climatetray.ui;

import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.MonitorListener;

public interface ClimateTrayController<MODEL_TYPE, VIEW_TYPE>
{

    void addMonitorListener(MonitorListener listener);

    void removeMonitorListener(MonitorListener listener);

    <TYPE> TYPE monitor(TYPE object);

    VIEW_TYPE getView();

    default void prepareWith(MODEL_TYPE model)
    {
        refreshWith(model);
    }

    void refreshWith(MODEL_TYPE model);

    default void modified(MessageBuffer messageBuffer)
    {
        // intentionally left blank
    }

    void applyTo(MODEL_TYPE model);

    void dismiss(MODEL_TYPE model);

}