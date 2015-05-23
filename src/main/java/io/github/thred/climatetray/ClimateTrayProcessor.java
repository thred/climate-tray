package io.github.thred.climatetray;

import io.github.thred.climatetray.util.ExceptionConsumer;
import io.github.thred.climatetray.util.VoidCallable;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ClimateTrayProcessor
{

    private final ScheduledExecutorService executor;

    private ScheduledFuture<?> updateFuture;

    public ClimateTrayProcessor()
    {
        super();

        executor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "Climate-Tray Executor Thread");

            thread.setUncaughtExceptionHandler((t, e) -> ClimateTray.LOG.error("Unhandled exception", e));

            return thread;
        });
    }

    public void scheduleUpdate(double updatePeriodInMinutes)
    {
        if (updateFuture != null)
        {
            ClimateTray.LOG.debug("Canceling existing update process.");

            updateFuture.cancel(false);
        }

        ClimateTray.LOG.info("Scheduling update every %.1f minutes.", updatePeriodInMinutes);

        updateFuture = executor.scheduleWithFixedDelay(() -> {
            try
            {
                ClimateTray.update();
            }
            catch (Exception e)
            {
                ClimateTray.LOG.error("Unhandled error while update", e);
            }
        }, 0, (long) (updatePeriodInMinutes * 60), TimeUnit.SECONDS);
    }

    public Future<?> submit(VoidCallable task)
    {
        return submit(task, null, null);
    }

    public Future<?> submit(VoidCallable task, VoidCallable onSuccess)
    {
        return submit(task, onSuccess, null);
    }

    public Future<?> submit(VoidCallable task, VoidCallable onSuccess, ExceptionConsumer onError)
    {
        return executor.submit(() -> {
            try
            {
                task.call();

                if (onSuccess != null)
                {
                    onSuccess.call();
                }
            }
            catch (Exception e)
            {
                if (onError != null)
                {
                    onError.failed(e);
                }

                throw e;
            }

            return null;
        });
    }

    public <RESULT_TYPE> Future<RESULT_TYPE> submit(Callable<RESULT_TYPE> task)
    {
        return submit(task, null);
    }

    public <RESULT_TYPE> Future<RESULT_TYPE> submit(Callable<RESULT_TYPE> task, Consumer<RESULT_TYPE> onSuccess)
    {
        return submit(task, onSuccess, null);
    }

    public <RESULT_TYPE> Future<RESULT_TYPE> submit(Callable<RESULT_TYPE> task, Consumer<RESULT_TYPE> onSuccess,
        ExceptionConsumer onError)
    {
        return executor.submit(() -> {
            try
            {
                RESULT_TYPE result = task.call();

                if (onSuccess != null)
                {
                    onSuccess.accept(result);
                }

                return result;
            }
            catch (Exception e)
            {
                if (onError != null)
                {
                    onError.failed(e);
                }

                throw e;
            }
        });
    }

    public void shutdown()
    {
        ClimateTray.LOG.info("Shutting down processor.");

        executor.shutdown();

        try
        {
            executor.awaitTermination(1, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            ClimateTray.LOG.warn("Shutdown of processor got interrupted");
        }
    }

}
