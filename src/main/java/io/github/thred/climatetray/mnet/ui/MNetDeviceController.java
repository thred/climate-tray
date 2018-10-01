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
package io.github.thred.climatetray.mnet.ui;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;

import java.awt.GridBagLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetEc;
import io.github.thred.climatetray.mnet.MNetInstallation;
import io.github.thred.climatetray.mnet.MNetUtils;
import io.github.thred.climatetray.ui.AbstractClimateTrayController;
import io.github.thred.climatetray.ui.ClimateTrayPresetListController;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.GBC;

public class MNetDeviceController extends AbstractClimateTrayController<MNetDevice, JPanel>
{

    private final JTextField nameField = monitor(createTextField("", 32));
    private final JComboBox<MNetInstallation> instalationField = monitor(createComboBox(MNetInstallation.values()));
    private final JCheckBox enabledBox = monitor(createCheckBox("Enabled"));
    private final JTextField hostField = monitor(createTextField("", 32));
    private final JComboBox<MNetEc> ecField = monitor(createComboBox(MNetEc.values()));
    private final JSpinner addressField = monitor(createSpinner(new SpinnerNumberModel(0, 0, 250, 1)));

    private final ClimateTrayPresetListController presetListController = monitor(new ClimateTrayPresetListController());

    public MNetDeviceController()
    {
        super();

        instalationField.setRenderer(new MNetInstallationCellRenderer());
    }

    @Override
    protected JPanel createView()
    {
        JPanel presetListView = presetListController.getView();

        JPanel view = new JPanel(new GridBagLayout());

        view.setOpaque(false);

        GBC gbc = new GBC(3, 11);

        view.add(createLabel("Custom Name:", nameField), gbc);
        view.add(nameField, gbc.next().span(2).hFill());

        view.add(enabledBox, gbc.next().next().span(2));

        view.add(createSeparator(), gbc.next().hFill().span(3));

        view
            .add(createHint(Message.info("Check the case of the air conditioner for the following fields.")),
                gbc.next().center().hFill().span(3));

        view.add(createLabel("Installation:", instalationField), gbc.next());
        view.add(instalationField, gbc.next().span(2));

        view.add(createLabel("Controller Address (IP):", hostField), gbc.next());
        view.add(hostField, gbc.next().span(2).hFill());

        view.add(createLabel("EC:", ecField), gbc.next());
        view.add(ecField, gbc.next().span(2));

        view.add(createLabel("Air Conditioner Address:", addressField), gbc.next());
        view.add(addressField, gbc.next().hFill());

        view.add(createSeparator(), gbc.next().next().hFill().span(3));

        view
            .add(createHint(Message
                .info(
                    "The following presets control this air conditioner only, without modifying the state of other devices. "
                        + "The defined presets can be found in the air conditioner's sub-menu of the popup menu.")),
                gbc.next().center().hFill().span(3));

        view.add(createLabel("Local Presets:", presetListView), gbc.next().top().insetTop(8));
        view.add(presetListView, gbc.next().span(2).weight(1, 1).fill());

        return view;
    }

    @Override
    public void refreshWith(MNetDevice model)
    {
        nameField.setText(Utils.ensure(model.getName(), ""));
        instalationField.setSelectedItem(Utils.ensure(model.getInstallation(), MNetInstallation.STANDING));
        enabledBox.setSelected(model.isEnabled());
        hostField.setText(Utils.ensure(model.getHost(), ""));
        ecField.setSelectedItem(Utils.ensure(model.getEc(), MNetEc.NONE));
        addressField.setValue(Utils.ensure(model.getAddress(), 0));

        presetListController.refreshWith(model.getPresets());
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        String name = nameField.getText().trim();

        if (name.length() <= 0)
        {
            messageBuffer.error("The field \"Custom Name\" is empty.");
        }

        String host = hostField.getText().trim();

        if (host.length() <= 0)
        {
            messageBuffer.error("The field \"Controller Address\" is empty.");
        }

        URL url = null;

        try
        {
            url = MNetUtils.toURL(host);
        }
        catch (MalformedURLException e)
        {
            messageBuffer.error("The value of the field \"Controller Address\" is invalid.");
        }

        if (url != null)
        {
            messageBuffer.info(url.toExternalForm());
        }

        if (!enabledBox.isSelected())
        {
            messageBuffer.warn("The air conditioner is not enabled!");
        }
    }

    @Override
    public void applyTo(MNetDevice model)
    {
        model.setName(nameField.getText().trim());
        model.setInstallation((MNetInstallation) instalationField.getSelectedItem());
        model.setEnabled(enabledBox.isSelected());
        model.setHost(hostField.getText().trim());
        model.setEc((MNetEc) ecField.getSelectedItem());
        model.setAddress((Integer) addressField.getValue());

        presetListController.applyTo(model.getPresets());
    }

    @Override
    public void dismiss(MNetDevice model)
    {
        // intentionally left blank
    }

}
