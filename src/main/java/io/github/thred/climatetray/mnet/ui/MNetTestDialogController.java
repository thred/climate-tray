package io.github.thred.climatetray.mnet.ui;

import io.github.thred.climatetray.mnet.ui.MNetTest.State;
import io.github.thred.climatetray.mnet.ui.MNetTest.Step;
import io.github.thred.climatetray.ui.DefaultClimateTrayDialogController;

import java.awt.Window;

public class MNetTestDialogController extends DefaultClimateTrayDialogController<MNetTest> implements
    MNetTestStepListener
{

    public MNetTestDialogController(Window owner)
    {
        super(owner, new MNetTestController(), Button.CANCEL, Button.CLOSE);

        setTitle("Testing Device");
    }

    @Override
    public void prepareWith(MNetTest model)
    {
        closeButton.setVisible(false);
        cancelButton.setVisible(true);

        model.addTestStepListener(this);

        super.prepareWith(model);

        model.start();
    }

    @Override
    public void testStep(MNetTest test, Step step, State state)
    {
        boolean finished = (state != State.RUNNING);

        closeButton.setVisible(finished);
        cancelButton.setVisible(!finished);
    }

    @Override
    public void cancel()
    {
        getModel().cancel();
    }
}
