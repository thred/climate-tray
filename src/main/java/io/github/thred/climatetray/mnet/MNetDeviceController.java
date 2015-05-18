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
package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.controller.AbstractClimateTrayController;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class MNetDeviceController extends AbstractClimateTrayController<MNetDevice, JPanel>
{

    private final JTextField nameField = monitor(createTextField("", 32));
    private final JTextField hostField = monitor(createTextField("", 32));
    private final JSpinner addressField = monitor(createSpinner(new SpinnerNumberModel(1, 1, 50, 1)));

    public MNetDeviceController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new GridBagLayout());
        GBC gbc = new GBC(3, 4);

        view.add(createLabel("Name:", nameField), gbc);
        view.add(nameField, gbc.next().span(2).hFill());

        view.add(createLabel("Host / URL:", hostField), gbc.next());
        view.add(hostField, gbc.next().span(2).hFill());

        view.add(createLabel("Address:", addressField), gbc.next());
        view.add(addressField, gbc.next().hFill());

        return view;
    }

    @Override
    public void prepare(MNetDevice model)
    {
        nameField.setText(Utils.ensure(model.getName(), ""));
        hostField.setText(Utils.ensure(model.getHost(), ""));
        addressField.setValue(Utils.ensure(model.getAddress(), 0));
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        String name = nameField.getText().trim();

        if (name.length() <= 0)
        {
            messageBuffer.error("The name is missing.");
        }

        String host = hostField.getText().trim();

        if (host.length() <= 0)
        {
            messageBuffer.error("The host / URL is missing.");
        }

        URL url = null;

        try
        {
            url = MNetUtils.toURL(host);
        }
        catch (MalformedURLException e)
        {
            messageBuffer.error("The host / URL is invalid.");
        }

        if (url != null)
        {
            messageBuffer.info(url.toExternalForm());
        }
    }

    @Override
    public void apply(MNetDevice model)
    {
        model.setName(nameField.getText().trim());
        model.setHost(hostField.getText().trim());
        model.setAddress((Integer) addressField.getValue());
    }

    @Override
    public void dismiss(MNetDevice model)
    {
        // intentionally left blank
    }

}
