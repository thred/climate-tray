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

import java.awt.Window;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import io.github.thred.climatetray.ui.AbstractClimateTrayWindowController.Button;
import io.github.thred.climatetray.ui.ClimateTrayMessageDialogController;
import io.github.thred.climatetray.util.BuildInfo;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.swing.FooterPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class ClimateTrayUtils
{

    private static final String BUILD_INFO_URL = "http://thred.github.io/climate-tray/VERSION";

    public static BuildInfo performBuildInfoRequest()
    {
        try
        {
            CloseableHttpClient client = ClimateTray.PREFERENCES.getProxySettings().createHttpClient();
            HttpGet request = new HttpGet(BUILD_INFO_URL);
            CloseableHttpResponse response;

            try
            {
                response = client.execute(request);
            }
            catch (IOException e)
            {
                ClimateTray.LOG.warn("Failed to request build information from \"%s\".", e, BUILD_INFO_URL);

                consumeUpdateFailed();

                return null;
            }

            try
            {
                int status = response.getStatusLine().getStatusCode();

                if ((status >= 200) && (status < 300))
                {
                    try (InputStream in = response.getEntity().getContent())
                    {
                        BuildInfo buildInfo = BuildInfo.create(in);

                        ClimateTray.LOG.info("Build Information: %s", buildInfo);

                        return buildInfo;
                    }
                }
                else
                {
                    ClimateTray.LOG.warn("Request to \"%s\" failed with error %d.", BUILD_INFO_URL, status);

                    consumeUpdateFailed();
                }
            }
            finally
            {
                response.close();
            }
        }
        catch (Exception e)
        {
            ClimateTray.LOG.warn("Failed to request build information.", e);
        }

        return null;
    }

    protected static void consumeUpdateFailed()
    {
        SwingUtilities.invokeLater(() -> {
            ClimateTrayUtils
                .dialogWithCloseAndProxySettings(null, "Request failed",
                    Message
                        .error("The request for version updates failed.\n\n"
                            + "This usually indicates, that the application cannot contact the website with the "
                            + "build information on GitHub. You may wish to update your proxy settings, now."));
        });
    }

    public static Button dialogWithOkButton(Window owner, String title, Message message)
    {
        return ClimateTrayMessageDialogController.consumeOkDialog(owner, title, message);
    }

    public static boolean dialogWithYesAndNoButtons(Window owner, String title, Message message)
    {
        return ClimateTrayMessageDialogController.consumeYesNoDialog(owner, title, message) == Button.YES;
    }

    public static Button dialogWithCloseAndProxySettings(Window owner, String title, Message message)
    {
        ClimateTrayMessageDialogController controller = new ClimateTrayMessageDialogController(owner, Button.CLOSE)
        {
            private final JButton proxyButton = SwingUtils.createButton("Proxy Settings", e -> proxySettings());

            @Override
            protected JComponent createBottomPanel(Button... buttons)
            {
                FooterPanel panel = (FooterPanel) super.createBottomPanel(buttons);

                panel.left(proxyButton);

                return panel;
            }

            public void proxySettings()
            {
                close();

                ClimateTrayService.proxySettings();
            }
        };

        controller.setTitle(title);

        return controller.consume(message);
    }
}
