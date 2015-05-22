package io.github.thred.climatetray.mnet.ui;

import io.github.thred.climatetray.mnet.ui.MNetTest.State;
import io.github.thred.climatetray.mnet.ui.MNetTest.Step;
import io.github.thred.climatetray.ui.AbstractClimateTrayController;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.MessageListener;
import io.github.thred.climatetray.util.swing.MessageComponent;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class MNetTestController extends AbstractClimateTrayController<MNetTest, JPanel> implements
    MNetTestStepListener, MessageListener
{

    private final MessageComponent messageComponent = new MessageComponent();
    private final JProgressBar progressBar = new JProgressBar();
    private final JPanel view = new JPanel(new BorderLayout());

    public MNetTestController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        view.add(messageComponent, BorderLayout.CENTER);
        view.add(progressBar, BorderLayout.SOUTH);

        return view;
    }

    @Override
    public void prepareWith(MNetTest model)
    {
        model.addTestStepListener(this);
        model.getMessages().addMessageListener(this);

        progressBar.setVisible(true);
        progressBar.setValue(0);
        progressBar.setMaximum(MNetTest.Step.values().length);
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        // intentionally left blank
    }

    @Override
    public void applyTo(MNetTest model)
    {
        // intentionally left blank
    }

    @Override
    public void dismiss(MNetTest model)
    {
        model.removeTestStepListener(this);
        model.getMessages().removeMessageListener(this);
    }

    @Override
    public void testStep(MNetTest test, Step step, State state)
    {
        boolean finished = (state != State.RUNNING);

        progressBar.setValue(step.ordinal() + 1);
        progressBar.setVisible(!finished);
    }

    @Override
    public void messageAdded(MessageBuffer messageBuffer, Message message)
    {
        messageComponent.setMessage(message);
    }

    @Override
    public void messagesCleared(MessageBuffer messageBuffer)
    {
        messageComponent.setMessage(null);
    }

    @Override
    public void messageRemoved(MessageBuffer messageBuffer, Message message)
    {
        // intentionally left blank
    }

    //
    //    protected void step(int index, Message message)
    //    {
    //        progressBar.setValue(index);
    //        messageComponent.setMessage(message);
    //    }
    //
    //    protected void result(Message message)
    //    {
    //        progressBar.setVisible(false);
    //        messageComponent.setMessage(message);
    //    }
}
