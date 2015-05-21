package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.swing.MessageComponent;

import java.awt.BorderLayout;

import javax.swing.JPanel;

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
    public void prepareWith(Message model)
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
