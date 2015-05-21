package io.github.thred.climatetray.mnet.ui;

import io.github.thred.climatetray.controller.DefaultClimateTrayDialogController;
import io.github.thred.climatetray.mnet.ui.MNetTest.Step;

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
    public void testStep(MNetTest test, Step step)
    {
        boolean finished = (test.isCanceled()) || (test.isFailed()) || (test.isSuccess());

        closeButton.setVisible(finished);
        cancelButton.setVisible(!finished);
    }

    @Override
    public void cancel()
    {
        getModel().cancel();
    }
}