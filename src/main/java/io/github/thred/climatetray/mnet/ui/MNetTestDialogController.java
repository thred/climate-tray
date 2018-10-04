package io.github.thred.climatetray.mnet.ui;

import java.awt.Window;

import io.github.thred.climatetray.mnet.ui.MNetTest.State;
import io.github.thred.climatetray.mnet.ui.MNetTest.Step;
import io.github.thred.climatetray.ui.DefaultClimateTrayDialogController;

public class MNetTestDialogController extends DefaultClimateTrayDialogController<MNetTest, MNetTestController>
    implements MNetTestStepListener
{

    public MNetTestDialogController(Window owner)
    {
        super(owner, new MNetTestController(), Button.YES, Button.NO, Button.CANCEL, Button.CLOSE);

        setTitle("Testing Air Conditioner Settings");
    }

    @Override
    public void refreshWith(MNetTest model)
    {
        closeButton.setVisible(false);
        cancelButton.setVisible(true);

        model.addTestStepListener(this);

        super.refreshWith(model);

        model.start();
    }

    @Override
    public void testStep(MNetTest test, Step step, State state)
    {
        boolean finished = (state != State.RUNNING);
        boolean asking = (!finished) && (step == Step.TOGGLING);

        yesButton.setVisible(asking);
        noButton.setVisible(asking);
        closeButton.setVisible(finished);
        cancelButton.setVisible(!finished);
    }

    @Override
    public void yes()
    {
        getModel().restoring(true);
    }

    @Override
    public void no()
    {
        getModel().restoring(false);
    }

    @Override
    public void cancel()
    {
        getModel().cancel();
    }
}
