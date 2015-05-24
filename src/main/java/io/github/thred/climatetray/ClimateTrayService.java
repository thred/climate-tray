package io.github.thred.climatetray;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.MNetService;
import io.github.thred.climatetray.mnet.MNetState;
import io.github.thred.climatetray.ui.ClimateTrayAboutDialogController;
import io.github.thred.climatetray.ui.ClimateTrayIconController;
import io.github.thred.climatetray.ui.ClimateTrayLogFrameController;
import io.github.thred.climatetray.ui.ClimateTrayPreferencesDialogController;
import io.github.thred.climatetray.util.ExceptionConsumer;
import io.github.thred.climatetray.util.VoidCallable;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.prefs.SystemPrefs;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.SwingUtilities;

public class ClimateTrayService
{

    private static final SystemPrefs PREFS = SystemPrefs.get(ClimateTray.class);
    private static final ScheduledExecutorService EXECUTOR;
    private static final ClimateTrayIconController ICON_CONTROLLER;
    private static final ClimateTrayAboutDialogController ABOUT_CONTROLLER;
    private static final ClimateTrayLogFrameController LOG_CONTROLLER;
    private static final ClimateTrayPreferencesDialogController PREFERENCES_CONTROLLER;

    static
    {
        EXECUTOR = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "Climate-Tray Executor Thread");

            thread.setUncaughtExceptionHandler((t, e) -> LOG.error("Unhandled exception", e));

            return thread;
        });

        ICON_CONTROLLER = new ClimateTrayIconController();
        ABOUT_CONTROLLER = new ClimateTrayAboutDialogController(null);
        LOG_CONTROLLER = new ClimateTrayLogFrameController(null);
        PREFERENCES_CONTROLLER = new ClimateTrayPreferencesDialogController(null);
    }

    private static ScheduledFuture<?> updateFuture;

    public static void prepare()
    {
        SwingUtilities.invokeLater(() -> ICON_CONTROLLER.prepareWith(PREFERENCES));
    }

    public static void load()
    {
        LOG.info("Loading preferences.");

        try
        {
            PREFERENCES.read(PREFS);
        }
        catch (Exception e)
        {
            LOG.error("Failed to load preferences", e);
            ClimateTrayUtils.okDialog(null, "Preferences", Message.error("Failed to load existing preferences."));
        }

        refresh();
    }

    public static void store()
    {
        PREFERENCES.write(PREFS);

        LOG.info("Preferences stored.");
    }

    public static void scheduleUpdate()
    {
        double updatePeriodInMinutes = PREFERENCES.getUpdatePeriodInMinutes();

        if (updateFuture != null)
        {
            LOG.debug("Canceling existing update process.");

            updateFuture.cancel(false);
        }

        LOG.info("Scheduling update every %.1f minutes.", updatePeriodInMinutes);

        updateFuture = EXECUTOR.scheduleWithFixedDelay(() -> {
            try
            {
                update();
            }
            catch (Exception e)
            {
                LOG.error("Unhandled error while update", e);
            }
        }, 0, (long) (updatePeriodInMinutes * 60), TimeUnit.SECONDS);
    }

    public static void update()
    {
        LOG.debug("Updating.");

        updateDevices();
    }

    public static void refresh()
    {
        ICON_CONTROLLER.refreshWith(PREFERENCES);
    }

    public static void updateDevices()
    {
        List<MNetDevice> devices = PREFERENCES.getDevices();

        devices.stream().forEach(device -> {
            MNetService.updateDevice(device);
            updatePresets();
        });
    }

    public static void updatePresets()
    {
        List<MNetDevice> devices = PREFERENCES.getDevices();
        List<MNetPreset> presets = PREFERENCES.getPresets();

        List<MNetState> states =
            devices.stream().filter(device -> device.isEnabled() && device.isSelected())
                .map(device -> device.getState()).collect(Collectors.toList());

        presets.stream().forEach(preset -> preset.setSelected(MNetService.isMatching(preset, states)));

        refresh();
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
        LOG.info("Shutting down processor.");

        EXECUTOR.shutdown();

        try
        {
            EXECUTOR.awaitTermination(1, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            LOG.warn("Shutdown of processor got interrupted");
        }
    }

    public static void togglePreset(UUID id)
    {
        LOG.debug("Toggling preset with id %s.", id);

        MNetPreset preset = PREFERENCES.getPreset(id);

        if (preset == null)
        {
            return;
        }

        submitTask(() -> PREFERENCES.getDevices().stream().filter(device -> device.isEnabled() && device.isSelected())
            .forEach(device -> MNetService.adjustDevice(device, preset)), ClimateTrayService::updatePresets);
    }

    public static void toggleDevice(UUID id)
    {
        LOG.debug("Toggling air conditioner with id %s.", id);

        MNetDevice device = PREFERENCES.getDevice(id);

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
        LOG.debug("Opening preferences dialog.");

        PREFERENCES_CONTROLLER.consume(PREFERENCES);
    }

    public static void log()
    {
        LOG.debug("Opening log frame.");

        LOG_CONTROLLER.consume(LOG);
    }

    public static void about()
    {
        LOG.debug("Opening about dialog.");

        ABOUT_CONTROLLER.consume(PREFERENCES);
    }

    public static void exit()
    {
        LOG.info("Exiting.");

        ICON_CONTROLLER.dismiss(PREFERENCES);
        ABOUT_CONTROLLER.dismiss(PREFERENCES);
        LOG_CONTROLLER.dismiss(LOG);
        PREFERENCES_CONTROLLER.dismiss(PREFERENCES);

        ClimateTrayService.shutdown();

        System.exit(0);
    }

}
