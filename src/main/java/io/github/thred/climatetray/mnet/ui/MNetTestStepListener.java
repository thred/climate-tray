package io.github.thred.climatetray.mnet.ui;

import io.github.thred.climatetray.mnet.ui.MNetTest.Step;

import java.util.EventListener;

public interface MNetTestStepListener extends EventListener
{

    void testStep(MNetTest test, Step step);

}
