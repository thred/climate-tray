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
package io.github.thred.climatetray.ui;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.util.Severity;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.message.MessageListener;
import io.github.thred.climatetray.util.swing.ButtonPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class ClimateTrayLogFrameController extends DefaultClimateTrayFrameController<MessageBuffer> implements
    MessageListener
{

    protected final JButton clearButton = SwingUtils.createButton("Clear", (e) -> clear());

    public ClimateTrayLogFrameController(Window owner)
    {
        super(owner, new ClimateTrayLogController(), Button.CLOSE);

        setTitle("Log");
    }

    @Override
    protected JComponent createContentPanel(JComponent view)
    {
        JScrollPane scrollPane = new JScrollPane(view);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    @Override
    protected JComponent createBottomPanel(Button... buttons)
    {
        ButtonPanel panel = (ButtonPanel) super.createBottomPanel(buttons);

        panel.left(clearButton);

        //        panel.left(SwingUtils.createButton("Test", (e) -> {
        //            Severity severity = Severity.values()[(int) (Math.random() * Severity.values().length)];
        //
        //            LOG.add(severity, "This is some test");
        //        }));

        return panel;
    }

    @Override
    public void prepareWith(MessageBuffer model)
    {
        model.addMessageListener(this);

        super.prepareWith(model);
    }

    @Override
    public void dismiss(MessageBuffer model)
    {

        model.removeMessageListener(this);

        LOG.debug("Closing log frame.");

        super.dismiss(model);
    }

    public void clear()
    {
        getModel().clear();
    }

    @Override
    public void messageAdded(MessageBuffer messageBuffer, Message message)
    {
        if (message.getSeverity().isCoveredBy(Severity.INFO))
        {
            setDescription(message);
        }
    }

    @Override
    public void messagesCleared(MessageBuffer messageBuffer)
    {
        setDescription((Message) null);
    }

    @Override
    public void messageRemoved(MessageBuffer messageBuffer, Message message)
    {
        // intentionally left blank
    }

}
