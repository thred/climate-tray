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
package io.github.thred.climatetray;

import io.github.thred.climatetray.ui.AbstractClimateTrayWindowController.Button;
import io.github.thred.climatetray.ui.ClimateTrayMessageDialogController;
import io.github.thred.climatetray.ui.ClimateTrayProxyDialogController;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.swing.ButtonPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.Window;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComponent;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

public class ClimateTrayUtils
{

    private static final String VERSION_URL = "http://thred.github.io/climate-tray/VERSION";

    public static Properties performVersionRequest()
    {
        try
        {
            CloseableHttpClient client = ClimateTray.PREFERENCES.getProxySettings().createHttpClient();
            HttpGet request = new HttpGet(VERSION_URL);
            CloseableHttpResponse response;

            try
            {
                response = client.execute(request);
            }
            catch (IOException e)
            {
                ClimateTray.LOG.warn("Failed to request version from \"%s\".", e, VERSION_URL);

                consumeUpdateFailed();

                return null;
            }

            try
            {
                int status = response.getStatusLine().getStatusCode();

                if ((status >= 200) && (status < 300))
                {
                    Properties properties = new Properties();

                    try (InputStream in = response.getEntity().getContent())
                    {
                        properties.load(in);
                    }

                    ClimateTray.LOG.info("Version Information: \n%s", properties.getProperty("build.version"));

                    return properties;
                }
                else
                {
                    ClimateTray.LOG.warn("Request to \"%s\" failed with error %d.", VERSION_URL, status);

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
            ClimateTray.LOG.warn("Failed to request version.", e);
        }

        return null;
    }

    protected static void consumeUpdateFailed()
    {
        ClimateTrayUtils.closeDialogWithProxySettings(null, "Request failed",
            Message.error("The request for the up-to-date version failed. You may wish to update the proxy settings."));
    }

    public static Button okDialog(Window owner, String title, Message message)
    {
        return ClimateTrayMessageDialogController.consumeOkDialog(owner, title, message);
    }

    public static Button closeDialogWithProxySettings(Window owner, String title, Message message)
    {
        ClimateTrayMessageDialogController controller = new ClimateTrayMessageDialogController(owner, Button.CLOSE)
        {
            private final JButton proxyButton = SwingUtils.createButton("Proxy Settings", e -> proxySettings());

            @Override
            protected JComponent createBottomPanel(Button... buttons)
            {
                ButtonPanel panel = (ButtonPanel) super.createBottomPanel(buttons);

                panel.left(proxyButton);

                return panel;
            }

            public void proxySettings()
            {
                close();

                ClimateTrayProxyDialogController controller = new ClimateTrayProxyDialogController(getView(), true);

                controller.consume(ClimateTray.PREFERENCES.getProxySettings());
            }
        };

        controller.setTitle(title);

        return controller.consume(message);
    }
}
