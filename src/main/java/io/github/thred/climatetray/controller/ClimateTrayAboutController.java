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

import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.MessageList;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;

public class ClimateTrayAboutController extends AbstractClimateTrayController<ClimateTrayPreferences, JPanel>
{

    private final JTextPane textPane = new JTextPane();
    private final JPanel view = new JPanel(new BorderLayout());

    public ClimateTrayAboutController()
    {
        super();

        textPane.setEditable(false);
        textPane.setOpaque(false);
        textPane.setPreferredSize(new Dimension(480, 320));
        textPane.setEditorKit(JEditorPane.createEditorKitForContentType("text/html"));
        textPane.addHyperlinkListener(event -> {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
            {
                if (Desktop.isDesktopSupported())
                {
                    try
                    {
                        Desktop.getDesktop().browse(event.getURL().toURI());
                    }
                    catch (IOException | URISyntaxException e)
                    {
                        e.printStackTrace(System.err);
                    }
                }
            }
        });
        
        try
        {
            textPane.setText(new String(Files.readAllBytes(Paths.get(getClass().getResource("about.html").toURI()))));
        }
        catch (IOException | URISyntaxException e)
        {
            throw new ExceptionInInitializerError(e);
        }

        view.add(textPane);
    }

    @Override
    public JPanel getView()
    {
        return view;
    }

    @Override
    public void prepare(ClimateTrayPreferences model)
    {
        // intentionally left blank
    }

    @Override
    public void apply(ClimateTrayPreferences model)
    {
        // intentionally left blank
    }

    @Override
    public void modified(MessageList messages)
    {
        // intentionally left blank
    }

}
