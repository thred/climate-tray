/*
 * Copyright (c) 2012 swing-on-fire Team
 *
 * This file is part of Swing-On-Fire (http://code.google.com/p/swing-on-fire), licensed under the terms of the MIT
 * License (MIT).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.thred.climatetray.util.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel
{

    private static final long serialVersionUID = -5243820877340066905L;

    private final JPanel panel;
    private final JPanel leftPanel;
    private final JPanel centerPanel;
    private final JPanel rightPanel;

    public ButtonPanel()
    {
        super(new BorderLayout());

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));

        panel = new BorderPanel(new GridBagLayout());
        panel.setOpaque(false);

        leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);

        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);

        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);

        GBC gbc = new GBC(3, 1).defaultOutsets(0, 0, 0, 0).defaultInsets(0, 0, 0, 0);
        panel.add(leftPanel, gbc.left().weight(0.5));
        panel.add(centerPanel, gbc.next().center().weight(0.5));
        panel.add(rightPanel, gbc.next().right().weight(0.5));

        add(panel, BorderLayout.CENTER);
    }

    public JPanel getPanel()
    {
        return panel;
    }

    public JPanel getLeftPanel()
    {
        return leftPanel;
    }

    public JPanel getCenterPanel()
    {
        return centerPanel;
    }

    public JPanel getRightPanel()
    {
        return rightPanel;
    }

    public ButtonPanel left(JComponent... components)
    {
        for (JComponent component : components)
        {
            leftPanel.add(component);
        }

        return this;
    }

    public ButtonPanel center(JComponent... components)
    {
        for (JComponent component : components)
        {
            centerPanel.add(component);
        }

        return this;
    }

    public ButtonPanel right(JComponent... components)
    {
        for (JComponent component : components)
        {
            rightPanel.add(component);
        }

        return this;
    }

}
