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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Copyable;
import io.github.thred.climatetray.util.swing.ButtonPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

public abstract class AbstractClimateTrayListSelectController<TYPE extends Copyable<TYPE>>
    extends AbstractClimateTrayListController<TYPE>
{

    protected final JLabel title = SwingUtils.createLabel(getTitle());

    protected final JButton upButton =
        SwingUtils.createIcon(ClimateTrayImage.ICON_UP.getIcon(ClimateTrayImageState.NONE, 16), null, e -> up());

    protected final JButton downButton =
        SwingUtils.createIcon(ClimateTrayImage.ICON_DOWN.getIcon(ClimateTrayImageState.NONE, 16), null, e -> down());

    protected final JButton removeButton = SwingUtils
        .createIcon(ClimateTrayImage.ICON_REMOVE.getIcon(ClimateTrayImageState.NONE, 16), null, e -> remove());

    public AbstractClimateTrayListSelectController()
    {
        super();
    }

    protected abstract String getTitle();

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new BorderLayout());

        view.setOpaque(false);

        ButtonPanel headerPanel = new ButtonPanel();

        headerPanel.left(title);
        headerPanel.right(upButton, downButton, removeButton);

        upButton.setVisible(false);
        downButton.setVisible(false);
        removeButton.setVisible(false);

        view.add(headerPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(list);

        scrollPane.setPreferredSize(new Dimension(320, 64));
        //        list.setBorder(BorderFactory.createEmptyBorder());

        view.add(scrollPane, BorderLayout.CENTER);

        return view;
    }

    @Override
    public void update()
    {
        int selectedIndex = (list != null) ? list.getSelectedIndex() : -1;
        boolean anySelected = selectedIndex >= 0;

        upButton.setEnabled(anySelected && selectedIndex > 0);
        downButton.setEnabled(anySelected && selectedIndex < listModel.getSize() - 1);
        removeButton.setEnabled(anySelected);
    }

    @Override
    public void doubleClicked()
    {
        fireSelectEvent(new SelectEvent<>(this, list.getSelectedValuesList(), true));

        update();
    }

    @Override
    public void selected()
    {
        fireSelectEvent(new SelectEvent<>(this, list.getSelectedValuesList(), false));

        update();
    }

}
