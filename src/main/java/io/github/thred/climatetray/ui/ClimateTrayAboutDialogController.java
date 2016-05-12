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
package io.github.thred.climatetray.ui;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.util.BuildInfo;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.swing.ButtonPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.Desktop;
import java.awt.Window;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JComponent;

public class ClimateTrayAboutDialogController extends DefaultClimateTrayDialogController<ClimateTrayPreferences>
{

    private final JButton visitHomepageButton = SwingUtils.createButton("Visit Homepage", e -> visitHomepage());

    public ClimateTrayAboutDialogController(Window owner)
    {
        super(owner, new ClimateTrayAboutController(), Button.CLOSE);

        setTitle("Climate Tray");
        setDescription("Simple control utility for A/Cs.");
    }

    @Override
    protected JComponent createBottomPanel(Button... buttons)
    {
        ButtonPanel panel = (ButtonPanel) super.createBottomPanel(buttons);

        panel.left(visitHomepageButton);

        return panel;
    }

    @Override
    public io.github.thred.climatetray.ui.AbstractClimateTrayWindowController.Button consume(
        ClimateTrayPreferences model)
    {
        ClimateTrayService.checkVersion(remoteBuildInfo -> {
            if (remoteBuildInfo == null)
            {
                return;
            }

            BuildInfo localBuildInfo = BuildInfo.createDefault();

            if (Objects.equals(localBuildInfo, remoteBuildInfo))
            {
                setDescription(Message.info("Your version of Climate-Tray is up-to-date."));
            }
            else
            {
                setDescription(Message.warn("A new version of Climate-Tray is available."));
            }
            
        });

        return super.consume(model);
    }

    @Override
    public void dismiss(ClimateTrayPreferences model)
    {
        LOG.debug("Closing about dialog.");

        super.dismiss(model);
    }

    public void visitHomepage()
    {
        try
        {
            LOG.info("Opening browser with URL: %s", ClimateTray.HOMEPAGE.toExternalForm());

            Desktop.getDesktop().browse(ClimateTray.HOMEPAGE.toURI());
        }
        catch (IOException | URISyntaxException e)
        {
            LOG.warn("Failed to open hyperlink", e);
        }
    }

}
