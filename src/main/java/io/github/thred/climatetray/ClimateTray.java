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
package io.github.thred.climatetray;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import io.github.thred.climatetray.util.message.MessageBuffer;

public class ClimateTray
{

    public static final MessageBuffer LOG = new MessageBuffer(true, 1024);
    public static final ClimateTrayPreferences PREFERENCES = new ClimateTrayPreferences();
    public static final URL HOMEPAGE;

    static
    {
        try
        {
            HOMEPAGE = new URL("http://thred.github.io/climate-tray/");
        }
        catch (MalformedURLException e)
        {
            throw new ExceptionInInitializerError(e);
        }
    }

    static
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException
            | UnsupportedLookAndFeelException e)
        {
            LOG.error("Failed to set LookAndFeel", e);
        }
    }

    public static void main(String[] arguments)
    {
        ClimateTrayService.prepare();
        ClimateTrayService.load();
        ClimateTrayService.scheduleUpdate();
        ClimateTrayService.checkVersion();
    }

}
