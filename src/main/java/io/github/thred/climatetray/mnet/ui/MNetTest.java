package io.github.thred.climatetray.mnet.ui;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDrive;
import io.github.thred.climatetray.mnet.MNetEc;
import io.github.thred.climatetray.mnet.MNetMode;
import io.github.thred.climatetray.mnet.MNetUtils;
import io.github.thred.climatetray.mnet.request.AbstractMNetDeviceRequest;
import io.github.thred.climatetray.mnet.request.MNetDeviceRequestItem;
import io.github.thred.climatetray.mnet.request.MNetInfoRequest;
import io.github.thred.climatetray.mnet.request.MNetMonitorRequest;
import io.github.thred.climatetray.mnet.request.MNetOperateRequest;
import io.github.thred.climatetray.mnet.request.MNetRequestException;
import io.github.thred.climatetray.util.ExceptionConsumer;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.Severity;

import java.net.MalformedURLException;
import java.util.stream.StreamSupport;

import javax.swing.event.EventListenerList;

public class MNetTest implements ExceptionConsumer
{

    public enum Step
    {
        VALIDATE,
        CHECK,
        FIX_EC,
        INFO,
        TOGGLING,
        RESTORING,
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

        if (step == Step.TOGGLING)
        {
            // undo the toggle operation
            ClimateTrayService.submitTask(() -> {
                MNetOperateRequest request = new MNetOperateRequest();
                boolean turnOn = (device.getState().getMode() == MNetMode.OFF);

                if (turnOn)
                {
                    request.turnOn(device);
                }
                else
                {
                    request.turnOff(device);
                }

                request.execute(device.getURL());
                updateDevice(request, device);
            });

            failed(new MNetTestException(Severity.WARN, "Test canceled."));
        }

        fireTestStep();
    }

    protected void ensureNotCanceled() throws MNetTestException
    {
        if (state == State.CANCELED)
        {
            throw new MNetTestException(Severity.WARN, "Test canceled.");
        }
    }

    @Override
    public void failed(Exception exception)
    {
        state = State.FAILED;

        if (exception instanceof MNetRequestException)
        {
            Message hint = ((MNetRequestException) exception).getHint();

            if (hint != null)
            {
                messages.add(hint);
            }
            else
            {
                messages.add(((MNetRequestException) exception).toMessage());
            }
        }

        if (exception instanceof MNetTestException)
        {
            messages.add(((MNetTestException) exception).toMessage());
        }

        LOG.error("Test failed.", exception);

        fireTestStep();
    }

    public void start()
    {
        messages.clear();

        state = State.RUNNING;
        fixedEc = false;

        ClimateTrayService.submitTask(() -> {
            step(Step.VALIDATE, "Validating the settings...");

            String host = device.getHost();

            if (host.length() <= 0)
            {
                throw new MNetTestException(Severity.ERROR, "The address of the centralized controller is missing.");
            }

            try
            {
                MNetUtils.toURL(host);
            }
            catch (MalformedURLException e)
            {
                throw new MNetTestException(Severity.ERROR, "The address of the centralized controller is invalid.");
            }
        }, this::check, this);
    }

    public void check()
    {
        ClimateTrayService.submitTask(() -> {
            ensureNotCanceled();
            step(Step.CHECK, "Calling %s...", device.describeSettings());

            MNetInfoRequest request = new MNetInfoRequest();

            request.addDevice(device).execute(device.getURL());
            updateDevice(request, device);
        }, this::fixEc, this);
    }

    public void fixEc()
    {
        ClimateTrayService
            .submitTask(
                () -> {
                    ensureNotCanceled();

                    if (isModelValid(device))
                    {
                        return;
                    }

                    step(Step.FIX_EC, "Trying to fix the EC value...");

                    MNetEc initialEc = device.getEc();

                    for (MNetEc ec : MNetEc.values())
                    {
                        if (initialEc == ec)
                        {
                            continue;
                        }

                        ensureNotCanceled();

                        LOG.info("Correcting EC to: %s", ec);

                        device.setEc(ec);

                        MNetInfoRequest request = new MNetInfoRequest();

                        request.addDevice(device).execute(device.getURL());
                        updateDevice(request, device);

                        if (isModelValid(device))
                        {
                            fixedEc = true;
                            return;
                        }
                    }

                    throw new MNetTestException(
                        Severity.ERROR,
                        "Failed to get the state of the air conditioner.\n\n"
                            + "The centralized controller was successfully contacted, but it seems, that the value of the field \"Air Conditioner Address\" is wrong. "
                            + "There is no air conditioner with the specified address.");
                }, this::info, this);
    }

    public void info()
    {
        ClimateTrayService
            .submitTask(
                () -> {
                    ensureNotCanceled();
                    step(Step.INFO, "Requesting current state...");

                    MNetMonitorRequest request = new MNetMonitorRequest();

                    request.addDevice(device).execute(device.getURL());
                    updateDevice(request, device);

                    if (device.getState().getMode() == null)
                    {
                        throw new MNetTestException(
                            Severity.ERROR,
                            "Failed to get the state of the air conditioner.\n\n"
                                + "The centralized controller was successfully contacted. It even found an air conditioner with the specified address, "
                                + "but the air conditioner did not return its state.");
                    }
                }, this::toggling, this);
    }

    public void toggling()
    {
        ClimateTrayService
            .submitTask(
                () -> {
                    ensureNotCanceled();

                    MNetOperateRequest request = new MNetOperateRequest();
                    boolean turnOn = (device.getState().getDrive() == MNetDrive.OFF);

                    if (turnOn)
                    {
                        step(Step.TOGGLING, "Turning the air conditioner on.\n\nDid the air conditioner turn on?");
                        request.turnOn(device);
                    }
                    else
                    {
                        step(Step.TOGGLING, "Turning the air conditioner off.\n\nDid the air conditioner turn off?");
                        request.turnOff(device);
                    }

                    request.execute(device.getURL());

                    MNetDeviceRequestItem item = updateDevice(request, device);

                    if (turnOn)
                    {
                        if (item.getDrive() != MNetDrive.ON)
                        {
                            throw new MNetTestException(
                                Severity.ERROR,
                                "Failed to turn the air conditioner on.\n\n"
                                    + "The centralized controller was successfully contacted. It even found an air conditioner with the specified address, "
                                    + "but turning it on failed. May be this operation is blocked by the administrator.");
                        }
                    }
                    else
                    {
                        if (item.getDrive() != MNetDrive.OFF)
                        {
                            throw new MNetTestException(
                                Severity.ERROR,
                                "Failed to turn the air conditioner off.\n\n"
                                    + "The centralized controller was successfully contacted. It even found an air conditioner with the specified address, "
                                    + "but turning it off failed. May be this operation is blocked by the administrator.");
                        }
                    }
                }, null, this);
    }

    public void restoring(boolean successful)
    {
        ClimateTrayService
            .submitTask(
                () -> {
                    ensureNotCanceled();

                    MNetOperateRequest request = new MNetOperateRequest();
                    boolean turnOn = (device.getState().getMode() == MNetMode.OFF);

                    if (turnOn)
                    {
                        step(Step.RESTORING, "Turning the air conditioner on, again.");
                        request.turnOn(device);
                    }
                    else
                    {
                        step(Step.RESTORING, "Turning the air conditioner off, again.");
                        request.turnOff(device);
                    }

                    request.execute(device.getURL());
                    updateDevice(request, device);

                    if (!successful)
                    {
                        String state = (turnOn) ? "off" : "on";

                        throw new MNetTestException(
                            Severity.ERROR,
                            "Failed to turn the air conditioner %1$s.\n\n"
                                + "The centralized controller was successfully contacted. It even found an air conditioner with the specified address and turned it %1$s. "
                                + "Unfortunately your air conditioner did not react. Are the values in the fields \"Controller Address\", \"EC\" and \"Air Conditioner Address\" really correct?",
                            state);
                    }
                }, this::success, this);
    }

    public void success()
    {
        state = State.SUCCEEDED;

        if (fixedEc)
        {
            step(Step.FINISHED, "The test succeeded. The EC value was corrected automatically to %s.", device.getEc());
        }
        else
        {
            step(Step.FINISHED, "The test succeeded.");
        }
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

    protected MNetDeviceRequestItem updateDevice(AbstractMNetDeviceRequest request, MNetDevice device)
        throws MNetTestException
    {
        MNetDeviceRequestItem item =
            StreamSupport
                .stream(request.spliterator(), false)
                .findFirst()
                .orElseThrow(
                    () -> new MNetTestException(Severity.ERROR,
                        "The centralized controller did not return any air conditioners.\n\n"
                            + "The request hit a server, but it may be the wrong one. "
                            + "You can check the log for the detailed exception."));

        item.update(device);

        return item;
    }

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
