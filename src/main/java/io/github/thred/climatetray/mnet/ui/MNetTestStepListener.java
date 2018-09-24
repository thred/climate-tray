package io.github.thred.climatetray.mnet.ui;

import java.util.EventListener;

import io.github.thred.climatetray.mnet.ui.MNetTest.State;
import io.github.thred.climatetray.mnet.ui.MNetTest.Step;

public interface MNetTestStepListener extends EventListener
{

    void testStep(MNetTest test, Step step, State state);

}
