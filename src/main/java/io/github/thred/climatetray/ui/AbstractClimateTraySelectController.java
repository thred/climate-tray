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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;

import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.AdvancedListModel;

public abstract class AbstractClimateTraySelectController<TYPE extends Copyable<TYPE>>
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
                clicked();
            }
        }
    };

    public AbstractClimateTraySelectController()
    {
        super();
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

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new BorderLayout());

        view.setOpaque(false);

        list.setVisibleRowCount(5);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener((e) -> {
            if (!e.getValueIsAdjusting())
            {
                selected();
            }
        });
        list.addMouseListener(mouseListener);

        JScrollPane scrollPane = new JScrollPane(list);

        scrollPane.setPreferredSize(new Dimension(320, 64));
        //        list.setBorder(BorderFactory.createEmptyBorder());

        view.add(scrollPane, BorderLayout.CENTER);

        return view;
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

    public void update()
    {
    }

    public void clicked()
    {
        fireSelectEvent(new SelectEvent<>(this, list.getSelectedValuesList(), true));

        update();
    }

    public void selected()
    {
        fireSelectEvent(new SelectEvent<>(this, list.getSelectedValuesList(), false));

        update();
    }

}
