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
