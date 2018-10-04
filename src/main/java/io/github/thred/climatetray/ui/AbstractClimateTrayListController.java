/*
 * Copyright 2015 - 2018 Manfred Hantschel
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;

import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.AdvancedListModel;

public abstract class AbstractClimateTrayListController<TYPE extends Copyable<TYPE>>
    extends AbstractClimateTrayController<List<TYPE>, JPanel>
{

    protected final AdvancedListModel<TYPE> listModel = new AdvancedListModel<>();
    protected final JList<TYPE> list = monitor(new JList<>(listModel));
    protected final EventListenerList listenerList = new EventListenerList();

    private final MouseListener mouseListener = new MouseAdapter()
    {
        @Override
        public void mouseClicked(MouseEvent e)
        {
            if (e.getClickCount() == 2)
            {
                doubleClicked();
            }
        }
    };

    public AbstractClimateTrayListController()
    {
        super();

        list.setVisibleRowCount(5);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener((e) -> {
            if (!e.getValueIsAdjusting())
            {
                selected();
            }
        });
        list.addMouseListener(mouseListener);
    }

    public void addSelectListener(SelectListener<TYPE> listener)
    {
        listenerList.add(SelectListener.class, listener);
    }

    public void removeSelectListener(SelectListener<TYPE> listener)
    {
        listenerList.remove(SelectListener.class, listener);
    }

    @SuppressWarnings("unchecked")
    protected void fireSelectEvent(SelectEvent<TYPE> event)
    {
        for (SelectListener<TYPE> listener : listenerList.getListeners(SelectListener.class))
        {
            listener.itemSelected(event);
        }
    }

    public void clearSelection()
    {
        list.clearSelection();
    }

    public TYPE getSelectedValue()
    {
        return list.getSelectedValue();
    }

    public void setSelectedValue(TYPE value)
    {
        if (value == null)
        {
            list.clearSelection();
        }
        else
        {
            list.setSelectedValue(value, true);
        }
    }

    @Override
    public void refreshWith(List<TYPE> model)
    {
        listModel.setList(Copyable.deepCopy(model));

        update();
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        update();
    }

    @Override
    public void applyTo(List<TYPE> model)
    {
        model.clear();

        Copyable.deepCopy(listModel.getList(), model);
    }

    @Override
    public void dismiss(List<TYPE> model)
    {
        // intentionally left blank
    }

    public void refresh()
    {
        listModel.refreshAllElements();

        update();
    }

    protected abstract String describe(TYPE element);

    public abstract void update();

    public void doubleClicked()
    {
        fireSelectEvent(new SelectEvent<>(this, list.getSelectedValuesList(), true));

        update();
    }

    public void selected()
    {
        fireSelectEvent(new SelectEvent<>(this, list.getSelectedValuesList(), false));

        update();
    }

    public void up()
    {
        int selectedIndex = list.getSelectedIndex();

        if (selectedIndex < 1)
        {
            return;
        }

        listModel.moveElementAt(selectedIndex, selectedIndex - 1);
        list.setSelectedIndex(selectedIndex - 1);
        list.ensureIndexIsVisible(selectedIndex - 1);
    }

    public void down()
    {
        int selectedIndex = list.getSelectedIndex();

        if ((selectedIndex < 0) || (selectedIndex >= (listModel.getSize() - 1)))
        {
            return;
        }

        listModel.moveElementAt(selectedIndex, selectedIndex + 1);
        list.setSelectedIndex(selectedIndex + 1);
        list.ensureIndexIsVisible(selectedIndex + 1);
    }

    public void remove()
    {
        int selectedIndex = list.getSelectedIndex();

        if (selectedIndex < 0)
        {
            return;
        }

        listModel.removeElementAt(selectedIndex);

        if (selectedIndex >= listModel.getSize())
        {
            selectedIndex = listModel.getSize() - 1;
        }

        if (selectedIndex >= 0)
        {
            list.setSelectedIndex(selectedIndex);
        }
    }
}
