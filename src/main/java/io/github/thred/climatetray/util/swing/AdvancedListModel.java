package io.github.thred.climatetray.util.swing;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

public class AdvancedListModel<TYPE> extends AbstractListModel<TYPE>
{

    private static final long serialVersionUID = -5104293143298311686L;

    private List<TYPE> list;

    public AdvancedListModel()
    {
        this(new ArrayList<>());
    }

    public AdvancedListModel(List<TYPE> list)
    {
        super();

        this.list = list;
    }

    public List<TYPE> getList()
    {
        return list;
    }

    public void setList(List<TYPE> list)
    {
        int size = this.list.size();

        this.list = list;

        fireContentsChanged(this, 0, Math.max(size, list.size()));
    }

    @Override
    public int getSize()
    {
        return list.size();
    }

    public TYPE addElement(TYPE element)
    {
        return addElementAt(getSize(), element);
    }

    public TYPE addElementAt(int index, TYPE element)
    {
        list.add(index, element);

        fireIntervalAdded(this, index, index);

        return element;
    }

    public int getIndexOfElement(TYPE element)
    {
        return list.indexOf(element);
    }

    @Override
    public TYPE getElementAt(int index)
    {
        return list.get(index);
    };

    public void moveElementAt(int fromIndex, int toIndex)
    {
        TYPE from = list.get(fromIndex);

        list.set(fromIndex, list.get(toIndex));
        list.set(toIndex, from);

        if (fromIndex < toIndex)
        {
            fireContentsChanged(this, fromIndex, toIndex);
        }
        else
        {
            fireContentsChanged(this, toIndex, fromIndex);
        }
    }

    public TYPE removeElementAt(int index)
    {
        TYPE element = list.remove(index);

        fireIntervalRemoved(this, index, index);

        return element;
    }

    public void refreshElement(TYPE element)
    {
        int index = getIndexOfElement(element);

        if (index >= 0)
        {
            refreshElementAt(index);
        }
    }

    public void refreshElementAt(int index)
    {
        fireContentsChanged(this, index, index);
    }

    public void refreshAllElements()
    {
        fireContentsChanged(this, 0, getSize());
    }
}
