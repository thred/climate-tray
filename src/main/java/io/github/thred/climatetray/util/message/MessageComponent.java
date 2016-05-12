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
package io.github.thred.climatetray.util.message;

import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class MessageComponent extends JPanel
{

    private static final long serialVersionUID = -8346274765018474917L;

    private final JTextArea messageArea = new JTextArea();
    private final JLabel iconLabel = new JLabel();

    private Message message = null;
    private int iconSize = 48;

    public MessageComponent()
    {
        super();

        setLayout(new GridBagLayout());
        setOpaque(false);

        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setOpaque(false);
        messageArea.setFont(iconLabel.getFont());
        messageArea.setEditable(false);
        messageArea.setRows(1);
        messageArea.setColumns(40);

        GBC gbc = new GBC(2, 1);

        add(iconLabel, gbc.top());
        add(messageArea, gbc.next().hFill().weight(1).center());
    }

    public MessageComponent(Message message)
    {
        this();

        setMessage(message);
    }

    public MessageComponent(int iconSize)
    {
        this();

        setIconSize(iconSize);
    }

    public MessageComponent(int iconSize, Message message)
    {
        this();

        setIconSize(iconSize);
        setMessage(message);
    }

    public Message getMessage()
    {
        return message;
    }

    public void setMessage(Message message)
    {
        setMessage((message != null) ? message.getSeverity().getImage().getIcon(ClimateTrayImageState.NONE, iconSize)
            : null, message);
    }

    public void setMessage(Icon icon, Message message)
    {
        this.message = message;

        messageArea.setText((message != null) ? message.getMessage() : "");
        iconLabel.setIcon(icon);

        Window window = SwingUtilities.getWindowAncestor(this);

        if (window != null)
        {
            SwingUtilities.invokeLater(() -> {
                window.pack();
                window.revalidate();
            });
        }
    }

    public Icon getIcon()
    {
        return iconLabel.getIcon();
    }

    public void setIcon(Icon icon)
    {
        iconLabel.setIcon(icon);
    }

    public int getIconSize()
    {
        return iconSize;
    }

    public void setIconSize(int iconSize)
    {
        this.iconSize = iconSize;
    }

    public JTextArea getMessageArea()
    {
        return messageArea;
    }

    public JLabel getIconLabel()
    {
        return iconLabel;
    }

}
