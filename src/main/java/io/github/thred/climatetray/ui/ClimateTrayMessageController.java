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

import java.awt.BorderLayout;

import javax.swing.JPanel;

import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.message.MessageComponent;

public class ClimateTrayMessageController extends AbstractClimateTrayController<Message, JPanel>
{

    private final MessageComponent messageComponent = new MessageComponent();
    private final JPanel view = new JPanel(new BorderLayout());

    public ClimateTrayMessageController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        view.add(messageComponent, BorderLayout.CENTER);

        return view;
    }

    @Override
    public void refreshWith(Message model)
    {
        messageComponent.setMessage(model);
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        // intentionally left blank
    }

    @Override
    public void applyTo(Message model)
    {
        // intentionally left blank
    }

    @Override
    public void dismiss(Message model)
    {
        // intentionally left blank
    }

}
