package io.github.thred.climatetray.mnet.ui;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetEc;
import io.github.thred.climatetray.mnet.MNetStateType;
import io.github.thred.climatetray.mnet.MNetUtils;
import io.github.thred.climatetray.mnet.request.MNetInfoRequest;
import io.github.thred.climatetray.mnet.request.MNetMonitorRequest;
import io.github.thred.climatetray.mnet.request.MNetRequestElement;
import io.github.thred.climatetray.util.ExceptionConsumer;
import io.github.thred.climatetray.util.MessageBuffer;

import java.net.MalformedURLException;

import javax.swing.event.EventListenerList;

public class MNetTest implements ExceptionConsumer
{

    public enum Step
    {
        VALIDATE,
        CHECK,
        FIX_EC,
        INFO,
        TOGGLE_A,
        TOGGLE_B,
        FINISHED;
    }

    public enum State
    {
        RUNNING,
        CANCELED,
        FAILED,
        SUCCEEDED
    }

    private final EventListenerList listenerList = new EventListenerList();
    private final MessageBuffer messages = new MessageBuffer();

    private final MNetDevice device;

    private Step step;
    private State state;
    private boolean fixedEc = false;

    public MNetTest(MNetDevice device)
    {
        super();

        this.device = device;
    }

    public void addTestStepListener(MNetTestStepListener listener)
    {
        listenerList.add(MNetTestStepListener.class, listener);
    }

    public void removeTestStepListener(MNetTestStepListener listener)
    {
        listenerList.remove(MNetTestStepListener.class, listener);
    }

    protected void fireTestStep()
    {
        for (MNetTestStepListener listener : listenerList.getListeners(MNetTestStepListener.class))
        {
            listener.testStep(this, step, state);
        }
    }

    public MessageBuffer getMessages()
    {
        return messages;
    }

    public Step getStep()
    {
        return step;
    }

    public void step(Step step, String message, Object... args)
    {
        checkCanceled();

        this.step = step;

        messages.info(message, args);

        fireTestStep();
    }

    public State getState()
    {
        return state;
    }

    public boolean isFixedEc()
    {
        return fixedEc;
    }

    public void cancel()
    {
        state = State.CANCELED;

        fireTestStep();
    }

    protected void checkCanceled()
    {
        if (state == State.CANCELED)
        {
            throw new MNetTestException("Test canceled.");
        }
    }

    @Override
    public void failed(Exception exception)
    {
        state = State.FAILED;

        if (exception instanceof MNetTestException)
        {
            if (exception.getMessage() != null)
            {
                messages.error(exception.getMessage());
            }
        }

        ClimateTray.LOG.error("Test failed", exception);

        fireTestStep();
    }

    public void start()
    {
        messages.clear();
        
        state = State.RUNNING;
        fixedEc = false;

        ClimateTray.PROCESSOR.submit(() -> {
            step(Step.VALIDATE, "Validating device...");

            String host = device.getHost();

            if (host.length() <= 0)
            {
                throw new MNetTestException("The host / URL is missing.");
            }

            try
            {
                MNetUtils.toURL(host);
            }
            catch (MalformedURLException e)
            {
                throw new MNetTestException("The host / URL is invalid.");
            }
        }, this::check, this);
    }

    public void check()
    {
        MNetInfoRequest request = new MNetInfoRequest();

        ClimateTray.PROCESSOR.submit(() -> {
            step(Step.CHECK, "Calling %s...", device.describe(true, MNetStateType.NONE));

            if (!request.execute(device, messages))
            {
                throw new MNetTestException();
            }

            device.setGroup(request.getGroup());
            device.setModel(request.getModel());
        }, this::fixEc, this);
    }

    public void fixEc()
    {
        MNetInfoRequest request = new MNetInfoRequest();

        ClimateTray.PROCESSOR.submit(() -> {
            checkCanceled();

            if (isModelValid(device))
            {
                return;
            }

            step(Step.CHECK, "Trying to fix the EC value...");

            MNetEc initialEc = device.getEc();

            for (MNetEc ec : MNetEc.values())
            {
                if (initialEc == ec)
                {
                    continue;
                }

                device.setEc(ec);

                if (!request.execute(device, messages))
                {
                    throw new MNetTestException();
                }

                device.setGroup(request.getGroup());
                device.setModel(request.getModel());

                if (isModelValid(device))
                {
                    fixedEc = true;
                    return;
                }
            }

            //            throw new MNetTestException("Failed to get the state of the device.\n\n"
            //                + "The server was successfully contacted, but it seems, that the address is wrong. "
            //                + "There is no device with the specified address.");
        }, this::info, this);
    }

    public void info()
    {
        MNetMonitorRequest request = new MNetMonitorRequest();

        ClimateTray.PROCESSOR.submit(() -> {
            step(Step.CHECK, "Requesting current state...");

            if (!request.execute(device, messages))
            {
                throw new MNetTestException();
            }

            for (MNetRequestElement element : request)
            {
                messages.info(element.toString());
            }
        }, this::success, this);
    }

    public void success()
    {
        state = State.SUCCEEDED;
        
        step(Step.FINISHED, "The test succeeded.");
    }

    //    protected void consumeCheck(ClimateTrayTest model)
    //    {
    //        MNetDevice device = model.getDevice();
    //
    //        model.setStep(Step.CHECK);
    //        messageComponent.setMessage(Message.info("Calling %s...", device.describe(true, MNetStateType.NONE)));
    //
    //    }
    //
    //    protected void consumeFixEc(MNetDevice device)
    //    {
    //
    //    }

    protected boolean isModelValid(MNetDevice device)
    {
        String model = device.getModel();

        if ((model == null) || ("*".equals(model)) || ("NONE".equals(model)))
        {
            return false;
        }

        Integer group = device.getGroup();

        if ((group == null) || ("*".equals(group)) || ("99".equals(group)))
        {
            return false;
        }

        return true;
    }
}
