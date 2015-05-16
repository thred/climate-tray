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
package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.MessageListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class ClimateTrayLogController extends AbstractClimateTrayController<MessageBuffer, JTextPane> implements
    MessageListener
{

    private final JTextPane view = new JTextPane();
    private final Style errorStyle;
    private final Style warnStyle;
    private final Style infoStyle;
    private final Style debugStyle;

    public ClimateTrayLogController()
    {
        super();

        view.setEditable(false);
        view.setPreferredSize(new Dimension(800, 450));
        view.setBackground(Color.BLACK);
        view.setForeground(Color.WHITE);

        StyleContext styleContext = new StyleContext();
        Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
        Style mainStyle = styleContext.addStyle("main", defaultStyle);

        StyleConstants.setFontFamily(mainStyle, Font.MONOSPACED);
        StyleConstants.setFontSize(mainStyle, 11);
        StyleConstants.setBold(mainStyle, true);

        errorStyle = styleContext.addStyle("error", mainStyle);

        StyleConstants.setForeground(errorStyle, new Color(0xc83737));

        warnStyle = styleContext.addStyle("warn", mainStyle);

        StyleConstants.setForeground(warnStyle, new Color(0xffcc00));

        infoStyle = styleContext.addStyle("info", mainStyle);

        StyleConstants.setForeground(infoStyle, new Color(0xffffff));

        debugStyle = styleContext.addStyle("debug", mainStyle);

        StyleConstants.setForeground(debugStyle, new Color(0xb3b3b3));
    }

    @Override
    public JTextPane getView()
    {
        return view;
    }

    @Override
    public void prepare(MessageBuffer model)
    {
        for (Message message : model)
        {
            messageAdded(message);
        }

        model.addMessageListener(this);
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        // intentionally left blank
    }

    @Override
    public void apply(MessageBuffer model)
    {
        // intentionally left blank
    }

    @Override
    public void dismiss(MessageBuffer model)
    {
        model.removeMessageListener(this);
    }

    @Override
    public void messageAdded(Message message)
    {
        Style style;

        switch (message.getSeverity())
        {
            case DEBUG:
                style = debugStyle;
                break;
            case ERROR:
                style = errorStyle;
                break;
            case INFO:
                style = infoStyle;
                break;
            case WARN:
                style = warnStyle;
                break;
            default:
                throw new UnsupportedOperationException("Severity not supported: " + message.getSeverity());
        }

        StyledDocument document = view.getStyledDocument();

        // view.insertIcon(message.getSeverity().getImage().getIcon(ClimateTrayImageState.NONE, 16));

        try
        {
            document.insertString(document.getLength(), message.getMessage() + "\n", style);
        }
        catch (BadLocationException e)
        {
            ClimateTray.LOG.error("Failed to add text. It will get funny now...", e);
        }

        view.setCaretPosition(document.getLength());
    }
}
