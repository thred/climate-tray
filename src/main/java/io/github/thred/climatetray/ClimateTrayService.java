/*
 * Copyright 2015, 2016 Manfred Hantschel
 * 
 * This file is part of Climate-Tray.
 * 
 * Climate-Tray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * Climate-Tray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with Climate-Tray. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package io.github.thred.climatetray;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.MNetService;
import io.github.thred.climatetray.mnet.MNetState;
import io.github.thred.climatetray.ui.ClimateTrayAboutDialogController;
import io.github.thred.climatetray.ui.ClimateTrayIconController;
import io.github.thred.climatetray.ui.ClimateTrayLogFrameController;
import io.github.thred.climatetray.ui.ClimateTrayMessageDialogController;
import io.github.thred.climatetray.ui.ClimateTrayPreferencesDialogController;
import io.github.thred.climatetray.ui.ClimateTrayProxyDialogController;
import io.github.thred.climatetray.util.BuildInfo;
import io.github.thred.climatetray.util.ExceptionConsumer;
import io.github.thred.climatetray.util.VoidCallable;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.prefs.SystemPrefs;
import io.github.thred.climatetray.util.swing.ButtonPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComponent;
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

    private volatile static ScheduledFuture<?> updateFuture;

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
            ClimateTrayUtils.dialogWithOkButton(null, "Preferences",
                Message.error("Failed to load existing preferences."));
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
        SwingUtilities.invokeLater(() -> ICON_CONTROLLER.refreshWith(PREFERENCES));
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

        devices.forEach(device -> device.getPresets().forEach(
            preset -> preset.setSelected(MNetService.isMatching(preset, device.getState()))));

        List<MNetState> states =
            devices.stream().filter(device -> device.isEnabled() && device.isSelectedAndWorking())
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
                else
                {
                    ClimateTray.LOG.error("Unhandled exception", e);
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

    public static void togglePreset(UUID presetId)
    {
        MNetDevice device = null;
        MNetPreset preset = PREFERENCES.getPreset(presetId);

        if (preset == null)
        {
            for (MNetDevice currentDevice : PREFERENCES.getDevices())
            {
                preset = currentDevice.getPreset(presetId);

                if (preset != null)
                {
                    device = currentDevice;
                    break;
                }
            }
        }

        if (preset == null)
        {
            return;
        }

        if (device == null)
        {
            togglePreset(preset);
        }
        else
        {
            togglePreset(device, preset);
        }
    }

    protected static void togglePreset(MNetPreset preset)
    {
        LOG.debug("Toggling preset with id %s for all selected devices.", preset.getId());

        submitTask(() -> PREFERENCES.getDevices().stream().filter(device -> device.isEnabled() && device.isSelected())
            .forEach(device -> MNetService.adjustDevice(device, preset)), ClimateTrayService::updatePresets);
    }

    protected static void togglePreset(MNetDevice device, MNetPreset preset)
    {
        LOG.debug("Toggling preset with id %s for device %s.", preset.getId(), device.getId());

        if (!device.isEnabled())
        {
            LOG.debug("Device is not enabled");

            return;
        }

        submitTask(() -> MNetService.adjustDevice(device, preset), ClimateTrayService::updatePresets);
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
        SwingUtilities.invokeLater(() -> {
            LOG.debug("Opening preferences dialog.");

            PREFERENCES_CONTROLLER.consume(PREFERENCES);
        });
    }

    public static void proxySettings()
    {
        SwingUtilities.invokeLater(() -> {
            LOG.debug("Opening proxy settings dialog.");

            if (PREFERENCES_CONTROLLER.getView().isVisible())
            {
                PREFERENCES_CONTROLLER.proxySettings();
            }
            else
            {
                ClimateTrayProxyDialogController controller = new ClimateTrayProxyDialogController(null, true);

                controller.consume(ClimateTray.PREFERENCES.getProxySettings());
            }
        });
    }

    public static void log()
    {
        SwingUtilities.invokeLater(() -> {
            LOG.debug("Opening log frame.");

            LOG_CONTROLLER.consume(LOG);
        });
    }

    public static void about()
    {
        SwingUtilities.invokeLater(() -> {
            LOG.debug("Opening about dialog.");

            ABOUT_CONTROLLER.consume(PREFERENCES);
        });
    }

    public static void checkVersion()
    {
        checkVersion(remoteBuildInfo -> {
            if (remoteBuildInfo == null)
            {
                return;
            }

            BuildInfo localBuildInfo = BuildInfo.createDefault();

            if (!Objects.equals(localBuildInfo, remoteBuildInfo))
            {
                ClimateTrayMessageDialogController controller = new ClimateTrayMessageDialogController(null)
                {
                    private final JButton visitHomepageButton = SwingUtils.createButton("Visit Homepage",
                        e -> visitHomepage());
                    private final JButton remindMeLaterButton = SwingUtils
                        .createButton("Remind Me Later", e -> close());
                    private final JButton disableCheckButton = SwingUtils.createButton("Disable Version Check",
                        e -> disableVersionCheck());

                    @Override
                    protected JComponent createBottomPanel(Button... buttons)
                    {
                        ButtonPanel panel = (ButtonPanel) super.createBottomPanel(buttons);

                        panel.left(visitHomepageButton);
                        panel.right(remindMeLaterButton, disableCheckButton);

                        return panel;
                    }

                    public void visitHomepage()
                    {
                        try
                        {
                            LOG.info("Opening browser with URL: %s", ClimateTray.HOMEPAGE.toExternalForm());

                            Desktop.getDesktop().browse(ClimateTray.HOMEPAGE.toURI());

                            close();
                        }
                        catch (IOException | URISyntaxException e)
                        {
                            LOG.warn("Failed to open hyperlink", e);
                        }
                    }

                    public void disableVersionCheck()
                    {
                        if (ClimateTrayUtils.dialogWithYesAndNoButtons(getView(), "Disable Version Check",
                            Message.warn("Are you sure, that you want to disable the version check?\n\n"
                                + "You can enable the check later in the preferences.")))
                        {
                            close();

                            if (PREFERENCES_CONTROLLER.getView().isVisible())
                            {
                                PREFERENCES_CONTROLLER.setVersionCheckEnabled(false);
                            }
                            else
                            {
                                ClimateTray.PREFERENCES.setVersionCheckEnabled(false);

                                store();
                            }
                        }
                    }
                };

                controller.setTitle("Version Update Check");

                controller.consume(Message.info("There is a new version available for download: %s.\n\n"
                    + "You are currently using version %s.", remoteBuildInfo, localBuildInfo));
            }
        });
    }

    public static void checkVersion(Consumer<BuildInfo> onSuccess)
    {
        if (!ClimateTray.PREFERENCES.isVersionCheckEnabled())
        {
            return;
        }

        submitTask(ClimateTrayUtils::performBuildInfoRequest, onSuccess);
    }

    public static void exit()
    {
        SwingUtilities.invokeLater(() -> {
            LOG.info("Exiting.");

            ICON_CONTROLLER.dismiss(PREFERENCES);
            ABOUT_CONTROLLER.dismiss(PREFERENCES);
            LOG_CONTROLLER.dismiss(LOG);
            PREFERENCES_CONTROLLER.dismiss(PREFERENCES);

            ClimateTrayService.shutdown();

            System.exit(0);
        });
    }

}
