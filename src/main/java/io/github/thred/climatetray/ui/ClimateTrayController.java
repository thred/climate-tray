package io.github.thred.climatetray.ui;

import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.MonitorListener;

public interface ClimateTrayController<MODEL_TYPE, VIEW_TYPE> {

	void addMonitorListener(MonitorListener listener);

	void removeMonitorListener(MonitorListener listener);

	<TYPE> TYPE monitor(TYPE object);

	VIEW_TYPE getView();

	void prepareWith(MODEL_TYPE model);

	void refreshWith(MODEL_TYPE model);

	void modified(MessageBuffer messageBuffer);

	void applyTo(MODEL_TYPE model);

	void dismiss(MODEL_TYPE model);

}