package io.github.thred.climatetray;

import io.github.thred.climatetray.controller.ClimateTrayPopupController;
import io.github.thred.climatetray.controller.ClimateTrayPreferencesDialogController;
import io.github.thred.climatetray.util.prefs.SystemPrefs;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClimateTray
{

    public static final SystemPrefs PREFS = SystemPrefs.get(ClimateTray.class);
    public static final ClimateTrayPreferences PREFERENCES = new ClimateTrayPreferences();

    private static final Timer TIMER = new Timer();

    private static final TrayIcon TRAY_ICON = new TrayIcon(ClimateTrayImage.ICON.getImage(ClimateTrayImageState.NOT_SELECTED, 16));
    private static final ClimateTrayPopupController POPUP_CONTROLLER = new ClimateTrayPopupController();

    public static void main(String[] arguments) throws ClassNotFoundException, InstantiationException,
        IllegalAccessException, UnsupportedLookAndFeelException
    {
        if (!SystemTray.isSupported())
        {
            throw new UnsupportedOperationException("SystemTray is not supported");
        }

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        TRAY_ICON.setImageAutoSize(true);
        TRAY_ICON.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.isPopupTrigger())
                {
                    popup(e.getX(), e.getY());
                }
            }
        });

        SystemTray tray = SystemTray.getSystemTray();

        try
        {
            tray.add(TRAY_ICON);
        }
        catch (AWTException e)
        {
            throw new ClimateTrayException("TrayIcon could not be added.", e);
        }

        load();

        start();
    }

    public static void load()
    {
        try
        {
            PREFERENCES.read(PREFS);
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);

            ClimateTrayUtils.errorDialog("Preferences", "Failed to load preferences.");
        }
    }

    public static void store()
    {
        PREFERENCES.write(PREFS);
    }

    public static void start()
    {
        TIMER.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                try
                {
                    update();
                }
                catch (Throwable e)
                {
                    e.printStackTrace(System.err);
                }
            }
        }, 0, PREFS.getLong("update-schedule-in-minutes", 1l) * 60 * 1000);
    }

    public static void stop()
    {
        TIMER.cancel();
    }

    public static void popup(int x, int y)
    {
        POPUP_CONTROLLER.consume(x, y);
    }

    public static void update()
    {
        System.out.println("Implement update");
    }

    public static void preferences()
    {
        ClimateTrayPreferencesDialogController controller = new ClimateTrayPreferencesDialogController();

        controller.consume(null, PREFERENCES);
    }

    public static void exit()
    {
        stop();

        SystemTray tray = SystemTray.getSystemTray();

        tray.remove(TRAY_ICON);

        System.exit(0);
    }

}
