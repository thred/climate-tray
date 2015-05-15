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
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class MNetDeviceController extends AbstractClimateTrayController<MNetDevice, JPanel>
{

    private final JTextField nameField = monitor(createTextField("", 32));
    private final JTextField hostField = monitor(createTextField("", 32));
    private final JSpinner addressField = monitor(createSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1)));
    private final JPanel view = new JPanel(new GridBagLayout());

    public MNetDeviceController()
    {
        super();

        GBC gbc = new GBC(3, 4);

        view.add(createLabel("Name:", nameField), gbc);
        view.add(nameField, gbc.next().span(2).hFill());

        view.add(createLabel("Host:", hostField), gbc.next());
        view.add(hostField, gbc.next().span(2).hFill());

        view.add(createLabel("Address:", addressField), gbc.next());
        view.add(addressField, gbc.next().hFill());
    }

    @Override
    public JPanel getView()
    {
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
    public void apply(MNetDevice model)
    {
        model.setName(nameField.getText().trim());
        model.setHost(hostField.getText().trim());
        model.setAddress((Integer) addressField.getValue());
    }

    @Override
    public void modified(MessageList messages)
    {
        String name = nameField.getText().trim();

        if (name.length() <= 0)
        {
            messages.addError("The name is missing.");
        }

        String host = hostField.getText().trim();

        if (host.length() <= 0)
        {
            messages.addError("The host is missing.");
        }
    }
}
