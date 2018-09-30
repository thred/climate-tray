package io.github.thred.climatetray.ui;

import java.util.EventListener;

public interface SelectListener<Any> extends EventListener
{

    void itemSelected(SelectEvent<Any> event);

}
