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

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import io.github.thred.climatetray.ClimateTrayUtils;
import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.swing.GBC;
import io.github.thred.climatetray.util.swing.SwingUtils;

public abstract class AbstractClimateTrayListEditController<TYPE extends Copyable<TYPE>>
    extends AbstractClimateTrayListController<TYPE>
{

    protected final JButton addButton = SwingUtils.createButton("Add...", (e) -> add());
    protected final JButton editButton = SwingUtils.createButton("Edit...", (e) -> edit());
    protected final JButton removeButton = SwingUtils.createButton("Remove", (e) -> remove());
    protected final JButton upButton = SwingUtils.createButton("Up", (e) -> up());
    protected final JButton downButton = SwingUtils.createButton("Down", (e) -> down());

    public AbstractClimateTrayListEditController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new GridBagLayout());

        view.setOpaque(false);

        GBC gbc = new GBC(2, 6).defaultOutsets(0, 0, 0, 0);

        JScrollPane scrollPane = new JScrollPane(list);

        scrollPane.setPreferredSize(new Dimension(480, 64));

        view.add(scrollPane, gbc.span(1, 6).weight(1, 1).fill());

        view.add(addButton, gbc.next().hFill().insetRight(0));
        view.add(editButton, gbc.next().hFill().insetRight(0));
        view.add(removeButton, gbc.next().hFill().insetRight(0));
        view.add(new JLabel(), gbc.next().weight(0, 1).insetRight(0));
        view.add(upButton, gbc.next().hFill().insetRight(0));
        view.add(downButton, gbc.next().hFill().insetRight(0));

        return view;
    }

    @Override
    public void update()
    {
        int selectedIndex = (list != null) ? list.getSelectedIndex() : -1;
        boolean anySelected = selectedIndex >= 0;

        addButton.setEnabled(true);
        editButton.setEnabled(anySelected);
        removeButton.setEnabled(anySelected);
        upButton.setEnabled(anySelected);
        downButton.setEnabled(anySelected);
    }

    @Override
    public void doubleClicked()
    {
        edit();
    }

    public void add()
    {
        int selectedIndex = (list != null) ? list.getSelectedIndex() : -1;
        TYPE element = createElement();

        if (consumeElement(element))
        {
            if (selectedIndex >= 0)
            {
                selectedIndex += 1;
            }

            listModel.addElementAt(selectedIndex, element);

            list.setSelectedValue(element, true);
        }
    }

    protected abstract TYPE createElement();

    public void edit()
    {
        TYPE element = list.getSelectedValue();

        if (element == null)
        {
            return;
        }

        if (consumeElement(element))
        {
            listModel.refreshElement(element);
        }
    }

    protected abstract boolean consumeElement(TYPE element);

    @Override
    public void remove()
    {
        int selectedIndex = list.getSelectedIndex();

        if (selectedIndex < 0)
        {
            return;
        }

        TYPE element = listModel.getElementAt(selectedIndex);

        if (ClimateTrayUtils
            .dialogWithYesAndNoButtons(SwingUtilities.getWindowAncestor(getView()), "Remove",
                Message.warn("Are you sure, that you want to remove the item \"%s\"?", describe(element))))
        {

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

}
