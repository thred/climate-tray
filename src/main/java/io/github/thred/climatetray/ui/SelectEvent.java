package io.github.thred.climatetray.ui;

import java.util.List;

public class SelectEvent<Any>
{

    private final Object source;
    private final List<Any> items;
    private final boolean doubleClick;

    public SelectEvent(Object source, List<Any> items, boolean doubleClick)
    {
        super();
        this.source = source;
        this.items = items;
        this.doubleClick = doubleClick;
    }

    public Object getSource()
    {
        return source;
    }

    public List<Any> getItems()
    {
        return items;
    }

    public boolean isDoubleClick()
    {
        return doubleClick;
    }

}
