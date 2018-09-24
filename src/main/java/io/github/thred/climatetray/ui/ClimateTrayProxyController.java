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

import static io.github.thred.climatetray.util.swing.SwingUtils.*;

import java.awt.GridBagLayout;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import io.github.thred.climatetray.ClimateTrayProxySettings;
import io.github.thred.climatetray.util.ProxyType;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.GBC;

public class ClimateTrayProxyController extends AbstractClimateTrayController<ClimateTrayProxySettings, JPanel>
{

    private final ButtonGroup proxyTypeGroup = new ButtonGroup();
    private final JRadioButton proxyTypeNoneRadio = monitor(createRadioButton("No Proxy", proxyTypeGroup));
    private final JRadioButton proxyTypeSystemDefaultRadio =
        monitor(createRadioButton("Use System Default Proxy", proxyTypeGroup));
    private final JRadioButton proxyTypeUserDefinedRadio =
        monitor(createRadioButton("User Defined Proxy", proxyTypeGroup));
    private final JTextField proxyHostField = monitor(createTextField("", 32));
    private final JSpinner proxyPortSpinner = monitor(createSpinner(new SpinnerNumberModel(80, 0, 65535, 1)));
    private final JCheckBox proxyAuthorizationNeededBox = monitor(createCheckBox("Proxy Authorization Needed"));
    private final JTextField proxyUserField = monitor(createTextField("", 32));
    private final JPasswordField proxyPasswordField = monitor(createPasswordField("", 32));
    private final JTextField proxyExcludesField = monitor(createTextField("", 32));

    private final JSpinner.NumberEditor proxyPortSpinnerEditor = new JSpinner.NumberEditor(proxyPortSpinner, "0");

    public ClimateTrayProxyController()
    {
        super();

        proxyPortSpinner.setEditor(proxyPortSpinnerEditor);
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new GridBagLayout());
        GBC gbc = new GBC(2, 10);

        view.add(proxyTypeNoneRadio, gbc.span(2));
        view.add(proxyTypeSystemDefaultRadio, gbc.next().span(2));
        view.add(proxyTypeUserDefinedRadio, gbc.next().span(2));

        view.add(createLabel("Proxy Host:", proxyHostField), gbc.next().insetLeft(48));
        view.add(proxyHostField, gbc.next().hFill());

        view.add(createLabel("Proxy Port:", proxyPortSpinner), gbc.next().insetLeft(48));
        view.add(proxyPortSpinner, gbc.next());

        view.add(createLabel("Proxy Excludes:", proxyExcludesField), gbc.next().top().insetTop(4).insetLeft(48));
        view.add(proxyExcludesField, gbc.next().hFill());
        view.add(createHint("A comma-separated list of hostnames/addresses with wildcards (*/?)."), gbc.next().next());

        view.add(proxyAuthorizationNeededBox, gbc.next().span(2).insetLeft(48));

        view.add(createLabel("Proxy User:", proxyUserField), gbc.next().insetLeft(96));
        view.add(proxyUserField, gbc.next().hFill());

        view.add(createLabel("Proxy Password:", proxyPasswordField), gbc.next().insetLeft(96));
        view.add(proxyPasswordField, gbc.next().hFill());

        return view;
    }

    @Override
    public void refreshWith(ClimateTrayProxySettings model)
    {
        proxyTypeNoneRadio.setSelected(model.getProxyType() == ProxyType.NONE);
        proxyTypeSystemDefaultRadio.setSelected(model.getProxyType() == ProxyType.SYSTEM_DEFAULT);
        proxyTypeUserDefinedRadio.setSelected(model.getProxyType() == ProxyType.USER_DEFINED);
        proxyHostField.setText(Utils.ensure(model.getProxyHost(), ""));
        proxyPortSpinner.setValue(Utils.ensure(model.getProxyPort(), 80));
        proxyAuthorizationNeededBox.setSelected(model.isProxyAuthorizationNeeded());
        proxyUserField.setText(Utils.ensure(model.getProxyUser(), ""));
        proxyPasswordField.setText(Utils.ensure(model.getProxyPassword(), ""));
        proxyExcludesField.setText(Utils.ensure(model.getProxyExcludes(), ""));
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        boolean userDefined = proxyTypeUserDefinedRadio.isSelected();

        proxyHostField.setEnabled(userDefined);
        proxyPortSpinner.setEnabled(userDefined);
        proxyAuthorizationNeededBox.setEnabled(userDefined);

        boolean authorizationNeeded = proxyAuthorizationNeededBox.isSelected();

        proxyUserField.setEnabled(userDefined && authorizationNeeded);
        proxyPasswordField.setEnabled(userDefined && authorizationNeeded);

        proxyExcludesField.setEnabled(userDefined);
        proxyExcludesField.setEditable(userDefined);

        if (userDefined)
        {
            if (Utils.isBlank(proxyHostField.getText()))
            {
                messageBuffer.error("The field \"Proxy Host\" is empty.");
            }

            if (authorizationNeeded)
            {
                if (Utils.isBlank(proxyUserField.getText()))
                {
                    messageBuffer.error("The field \"Proxy User\" is empty.");
                }

                if (Utils.isBlank(String.valueOf(proxyPasswordField.getPassword())))
                {
                    messageBuffer.error("The field \"Proxy Password\" is empty.");
                }
            }
        }
    }

    @Override
    public void applyTo(ClimateTrayProxySettings model)
    {
        if (proxyTypeNoneRadio.isSelected())
        {
            model.setProxyType(ProxyType.NONE);
        }
        else if (proxyTypeSystemDefaultRadio.isSelected())
        {
            model.setProxyType(ProxyType.SYSTEM_DEFAULT);
        }
        else if (proxyTypeUserDefinedRadio.isSelected())
        {
            model.setProxyType(ProxyType.USER_DEFINED);
        }

        model.setProxyHost(proxyHostField.getText());
        model.setProxyPort((Integer) proxyPortSpinner.getValue());
        model.setProxyAuthorizationNeeded(proxyAuthorizationNeededBox.isSelected());
        model.setProxyUser(proxyUserField.getText());
        model.setProxyPassword(String.valueOf(proxyPasswordField.getPassword()));
        model.setProxyExcludes(proxyExcludesField.getText());
    }

    @Override
    public void dismiss(ClimateTrayProxySettings model)
    {
        // intentionally left blank
    }

}
