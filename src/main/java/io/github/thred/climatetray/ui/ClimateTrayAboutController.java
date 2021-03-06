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

import static io.github.thred.climatetray.ClimateTray.*;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;

import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.message.MessageBuffer;

public class ClimateTrayAboutController extends AbstractClimateTrayController<ClimateTrayPreferences, JPanel>
{

    private final JTextPane textPane = new JTextPane();

    public ClimateTrayAboutController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new BorderLayout());

        view.setOpaque(false);

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
                        URL url = event.getURL();

                        LOG.info("Opening browser with URL: %s", url);

                        Desktop.getDesktop().browse(url.toURI());
                    }
                    catch (IOException | URISyntaxException e)
                    {
                        LOG.warn("Failed to open hyperlink", e);
                    }
                }
            }
        });

        try
        {
            textPane.setText(new String(Utils.toByteArray(getClass().getResourceAsStream("about.html")), "UTF-8"));
        }
        catch (IOException e)
        {
            throw new ExceptionInInitializerError(e);
        }

        view.add(textPane);

        return view;
    }

    @Override
    public void refreshWith(ClimateTrayPreferences model)
    {
        // intentionally left blank
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        // intentionally left blank
    }

    @Override
    public void applyTo(ClimateTrayPreferences model)
    {
        // intentionally left blank
    }

    @Override
    public void dismiss(ClimateTrayPreferences model)
    {
        // intentionally left blank
    }

}
