/*
 * Copyright 2015, 2016 Manfred Hantschel
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

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BorderPanel extends JPanel
{

    private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(8, 12, 8, 12);
    private static final long serialVersionUID = -3653014923804516104L;

    public BorderPanel(LayoutManager layoutManager)
    {
        this(layoutManager, DEFAULT_BORDER, null);
    }

    public BorderPanel(LayoutManager layoutManager, Border border)
    {
        this(layoutManager, border, null);
    }

    public BorderPanel(JComponent component)
    {
        this(new BorderLayout(), DEFAULT_BORDER, component);
    }

    public BorderPanel(Border border, JComponent component)
    {
        this(new BorderLayout(), border, component);
    }

    protected BorderPanel(LayoutManager layoutManager, Border border, JComponent component)
    {
        super(layoutManager);

        setOpaque(false);

        if (border != null)
        {
            setBorder(border);
        }

        if (component != null)
        {
            add(component, BorderLayout.CENTER);
        }
    }

}
