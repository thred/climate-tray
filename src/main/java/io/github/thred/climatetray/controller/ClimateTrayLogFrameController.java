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
package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.MessageListener;
import io.github.thred.climatetray.util.Severity;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class ClimateTrayLogFrameController extends
    AbstractClimateTrayFrameController<MessageBuffer, ClimateTrayLogController> implements MessageListener
{

    public ClimateTrayLogFrameController()
    {
        super(new ClimateTrayLogController(), Button.CLOSE);

        setTitle("Log");
    }

    //    @Override
    //    protected JComponent createBottomPanel(Button... buttons)
    //    {
    //        JComponent result = super.createBottomPanel(buttons);
    //
    //        ((ButtonPanel) result).left(SwingUtils.createButton("Test", (e) -> {
    //            Severity severity = Severity.values()[(int) (Math.random() * Severity.values().length)];
    //
    //            ClimateTray.LOG.add(severity, "This is some test");
    //        }));
    //
    //        return result;
    //    }

    @Override
    protected JComponent createContentPanel(JComponent view)
    {
        JScrollPane scrollPane = new JScrollPane(view);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    @Override
    public void prepare(MessageBuffer model)
    {
        model.addMessageListener(this);

        super.prepare(model);
    }

    @Override
    public void dismiss(MessageBuffer model)
    {
        model.removeMessageListener(this);

        ClimateTray.LOG.debug("Closing log frame.");

        super.dismiss(model);

    }

    @Override
    public void messageAdded(Message message)
    {
        if (message.getSeverity().ordinal() >= Severity.INFO.ordinal())
        {
            setDescription(message);
        }
    }

}
