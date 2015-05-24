package io.github.thred.climatetray.ui;

import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.message.MessageComponent;

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
