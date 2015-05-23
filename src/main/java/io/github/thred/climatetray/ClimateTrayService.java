package io.github.thred.climatetray;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDeviceService;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.ui.ClimateTrayAboutDialogController;
import io.github.thred.climatetray.ui.ClimateTrayLogFrameController;
import io.github.thred.climatetray.ui.ClimateTrayPreferencesDialogController;
import io.github.thred.climatetray.util.ExceptionConsumer;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.VoidCallable;
import io.github.thred.climatetray.util.prefs.SystemPrefs;

import java.awt.Image;
import java.awt.SystemTray;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ClimateTrayService
{

    private static final SystemPrefs PREFS = SystemPrefs.get(ClimateTray.class);
    private static final ScheduledExecutorService EXECUTOR;
    private static final ClimateTrayAboutDialogController ABOUT_CONTROLLER;
    private static final ClimateTrayLogFrameController LOG_CONTROLLER;
    private static final ClimateTrayPreferencesDialogController PREFERENCES_CONTROLLER;

    static
    {
        EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "Climate-Tray Executor Thread");

            thread.setUncaughtExceptionHandler((t, e) -> ClimateTray.LOG.error("Unhandled exception", e));

            return thread;
        });

        ABOUT_CONTROLLER = new ClimateTrayAboutDialogController(null);
        LOG_CONTROLLER = new ClimateTrayLogFrameController(null);
        PREFERENCES_CONTROLLER = new ClimateTrayPreferencesDialogController(null);
    }

    private static ScheduledFuture<?> updateFuture;

    public static void load()
    {
        ClimateTray.LOG.info("Loading preferences.");

        try
        {
            ClimateTray.PREFERENCES.read(PREFS);
        }
        catch (Exception e)
        {
            ClimateTray.LOG.error("Failed to load preferences", e);
            ClimateTrayUtils.okDialog(null, "Preferences", Message.error("Failed to load existing preferences."));
        }
    }

    public static void store()
    {
        ClimateTray.PREFERENCES.write(PREFS);

        ClimateTray.LOG.info("Preferences stored.");
    }

    public static void scheduleUpdate()
    {
        double updatePeriodInMinutes = ClimateTray.PREFERENCES.getUpdatePeriodInMinutes();

        if (updateFuture != null)
        {
            ClimateTray.LOG.debug("Canceling existing update process.");

            updateFuture.cancel(false);
        }

        ClimateTray.LOG.info("Scheduling update every %.1f minutes.", updatePeriodInMinutes);

        updateFuture = EXECUTOR.scheduleWithFixedDelay(() -> {
            try
            {
                update();
            }
            catch (Exception e)
            {
                ClimateTray.LOG.error("Unhandled error while update", e);
            }
        }, 0, (long) (updatePeriodInMinutes * 60), TimeUnit.SECONDS);
    }

    public static void update()
    {
        ClimateTray.LOG.debug("Updating.");

        List<MNetDevice> devices = ClimateTray.PREFERENCES.getDevices();

        devices.stream().forEach(device -> MNetDeviceService.update(device));

        MNetDevice activeDevice =
            devices.stream().filter(device -> (device.isEnabled()) && (device.isSelected())).findFirst().orElse(null);

        if (activeDevice != null)
        {
            Image image =
                activeDevice.getState().createImage(ClimateTrayImageState.NOT_SELECTED, ClimateTray.TRAY_ICON_SIZE);
            String toolTip = activeDevice.describeState();

            ClimateTray.updateTrayIcon(image, toolTip);
        }
        else
        {
            ClimateTray.updateTrayIcon(null, null);
        }
    }

    public static Future<?> submitTask(VoidCallable task)
    {
        return submitTask(task, null, null);
    }

    public static Future<?> submitTask(VoidCallable task, VoidCallable onSuccess)
    {
        return submitTask(task, onSuccess, null);
    }

    public static Future<?> submitTask(VoidCallable task, VoidCallable onSuccess, ExceptionConsumer onError)
    {
        return EXECUTOR.submit(() -> {
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

    public static <RESULT_TYPE> Future<RESULT_TYPE> submitTask(Callable<RESULT_TYPE> task)
    {
        return submitTask(task, null);
    }

    public static <RESULT_TYPE> Future<RESULT_TYPE> submitTask(Callable<RESULT_TYPE> task,
        Consumer<RESULT_TYPE> onSuccess)
    {
        return submitTask(task, onSuccess, null);
    }

    public static <RESULT_TYPE> Future<RESULT_TYPE> submitTask(Callable<RESULT_TYPE> task,
        Consumer<RESULT_TYPE> onSuccess, ExceptionConsumer onError)
    {
        return EXECUTOR.submit(() -> {
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

    public static void shutdown()
    {
        ClimateTray.LOG.info("Shutting down processor.");

        EXECUTOR.shutdown();

        try
        {
            EXECUTOR.awaitTermination(1, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            ClimateTray.LOG.warn("Shutdown of processor got interrupted");
        }
    }

    public static void togglePreset(UUID id)
    {
        ClimateTray.LOG.debug("Toggling preset with id %s.", id);

        ClimateTray.PREFERENCES.getPresets().stream().forEach((preset) -> preset.setSelected(false));

        MNetPreset preset = ClimateTray.PREFERENCES.getPreset(id);

        if (preset == null)
        {
            return;
        }

        // TODO
        preset.setSelected(true);

        ClimateTray.LOG.debug(preset.toString());
    }

    public static void toggleDevice(UUID id)
    {
        ClimateTray.LOG.debug("Toggling air conditioner with id %s.", id);

        MNetDevice device = ClimateTray.PREFERENCES.getDevice(id);

        if (device == null)
        {
            return;
        }

        device.setSelected(!device.isSelected());

        store();
        scheduleUpdate();
    }

    public static void preferences()
    {
        ClimateTray.LOG.debug("Opening preferences dialog.");

        PREFERENCES_CONTROLLER.consume(ClimateTray.PREFERENCES);
    }

    public static void log()
    {
        ClimateTray.LOG.debug("Opening log frame.");

        LOG_CONTROLLER.consume(ClimateTray.LOG);
    }

    public static void about()
    {
        ClimateTray.LOG.debug("Opening about dialog.");

        ABOUT_CONTROLLER.consume(ClimateTray.PREFERENCES);
    }

    public static void exit()
    {
        ClimateTray.LOG.info("Exiting.");

        SystemTray tray = SystemTray.getSystemTray();

        tray.remove(ClimateTray.TRAY_ICON);

        ClimateTrayService.shutdown();

        System.exit(0);
    }

}
