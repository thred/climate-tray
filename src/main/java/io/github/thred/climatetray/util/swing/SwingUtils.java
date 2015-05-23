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

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;

public class SwingUtils
{

    public static JMenuItem createMenuItem(String text, Icon icon, String toolTip, ActionListener... listeners)
    {
        JMenuItem result = new JMenuItem(text, icon);

        if (toolTip != null)
        {
            result.setToolTipText(toolTip);
        }

        for (ActionListener listener : listeners)
        {
            result.addActionListener(listener);
        }

        return result;
    }

    public static JCheckBoxMenuItem createCheckBoxMenuItem(String text, Icon icon, String toolTip,
        ActionListener... listeners)
    {
        JCheckBoxMenuItem result = new JCheckBoxMenuItem(text, icon);

        if (toolTip != null)
        {
            result.setToolTipText(toolTip);
        }

        for (ActionListener listener : listeners)
        {
            result.addActionListener(listener);
        }

        return result;
    }

    public static JRadioButtonMenuItem createRadioButtonMenuItem(String text, Icon icon, String toolTip,
        ActionListener... listeners)
    {
        JRadioButtonMenuItem result = new JRadioButtonMenuItem(text, icon);

        if (toolTip != null)
        {
            result.setToolTipText(toolTip);
        }

        for (ActionListener listener : listeners)
        {
            result.addActionListener(listener);
        }

        return result;
    }

    public static JButton createButton(String text, ActionListener... listeners)
    {
        JButton result = new JButton(text);

        for (ActionListener listener : listeners)
        {
            result.addActionListener(listener);
        }

        return result;
    }

    public static JLabel createLabel(String text)
    {
        return createLabel(text, null);
    }

    public static JLabel createLabel(String text, Component labelFor)
    {
        JLabel result = new JLabel(text);

        if (labelFor != null)
        {
            result.setLabelFor(labelFor);
        }

        return result;
    }

    public static JLabel createHint(String text)
    {
        JLabel result = new JLabel(text);

        try
        {
            result.setFont(result.getFont().deriveFont(Font.ITALIC));
        }
        catch (NullPointerException e)
        {
            // ignore, some LookAndFeels cause this, because the font is not initialized
        }

        return result;
    }

    public static JTextField createTextField(String text, int columns, ActionListener... listeners)
    {
        JTextField result = new JTextField(text, columns);

        for (ActionListener listener : listeners)
        {
            result.addActionListener(listener);
        }

        return result;
    }

    public static JCheckBox createCheckBox(String text, ActionListener... listeners)
    {
        JCheckBox result = new JCheckBox(text);

        for (ActionListener listener : listeners)
        {
            result.addActionListener(listener);
        }

        return result;
    }

    public static JSpinner createSpinner(SpinnerModel model)
    {
        JSpinner result = new JSpinner(model);

        return result;
    }

    public static <TYPE> JComboBox<TYPE> createComboBox(TYPE[] items, ActionListener... listeners)
    {
        JComboBox<TYPE> result = new JComboBox<>(items);

        for (ActionListener listener : listeners)
        {
            result.addActionListener(listener);
        }

        return result;
    }
}
