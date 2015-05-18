package io.github.thred.climatetray;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClimateTrayProcessor
{

    private final ScheduledExecutorService executor;

    private ScheduledFuture<?> updateFuture;

    public ClimateTrayProcessor()
    {
        super();

        executor = Executors.newSingleThreadScheduledExecutor();
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

    public Future<?> submit(Runnable task)
    {
        return executor.submit(task);
    }

    public <RESULT_TYPE> Future<RESULT_TYPE> submit(Callable<RESULT_TYPE> task)
    {
        return executor.submit(task);
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
