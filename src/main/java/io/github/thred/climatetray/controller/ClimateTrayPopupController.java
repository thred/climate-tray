package io.github.thred.climatetray.controller;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.Component;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.WindowConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ClimateTrayPopupController extends AbstractClimateTrayController<ClimateTrayPreferences, JPopupMenu>
    implements PopupMenuListener
{

    private final JMenuItem preferencesItem = createMenuItem("Preferences...", null,
        "Manage the presets, devices and other settings.");
    private final JMenuItem exitItem = createMenuItem("Exit", null, null);
    private final List<Component> dynamicItems = new ArrayList<>();
    private final JPopupMenu view = new JPopupMenu("Climate Tray");

    private JDialog hiddenDialogForFocusManagement;

    public ClimateTrayPopupController()
    {
        super();

        preferencesItem.addActionListener((e) -> ClimateTray.preferences());
        exitItem.addActionListener((e) -> ClimateTray.exit());

        view.addPopupMenuListener(this);

        view.add(preferencesItem);
        view.add(exitItem);
    }

    @Override
    public JPopupMenu getView()
    {
        return view;
    }

    @Override
    public void prepare(ClimateTrayPreferences model)
    {
        JPopupMenu view = getView();
        int index = 0;

        dynamicItems.stream().forEach((item) -> view.remove(item));
        dynamicItems.clear();

        index = preparePresets(model, view, index);
        prepareDevices(model, view, index);

    }

    protected int preparePresets(ClimateTrayPreferences model, JPopupMenu view, int index)
    {
        ButtonGroup group = new ButtonGroup();
        List<MNetPreset> presets = model.getPresets();

        if (presets.size() > 0)
        {
            for (MNetPreset preset : presets)
            {
                Icon icon = createIcon(preset);
                JRadioButtonMenuItem item =
                    SwingUtils.createRadioButtonMenuItem(preset.describe(), icon, null, (e) -> presetSelect(preset));

                item.setName(preset.getId().toString());

                view.add(item, index++);
                group.add(item);
                dynamicItems.add(item);
            }

            dynamicItems.add(view.add(new JPopupMenu.Separator(), index++));
        }
        return index;
    }

    protected int prepareDevices(ClimateTrayPreferences model, JPopupMenu view, int index)
    {
        List<MNetDevice> devices = model.getDevices();

        if (devices.size() > 0)
        {
            for (MNetDevice device : devices)
            {
                Icon icon = createIcon(device);
                JCheckBoxMenuItem item =
                    SwingUtils.createCheckBoxMenuItem(device.describe(false, true), icon, device.describe(true, false),
                        (e) -> deviceSelect(device));

                item.setName(device.getId().toString());
                item.setSelected(device.isEnabled());

                dynamicItems.add(view.add(item, index++));
            }

            dynamicItems.add(view.add(new JPopupMenu.Separator(), index++));
        }

        return index;
    }

    protected Icon createIcon(MNetPreset preset)
    {
        ClimateTrayImageState imageState =
            (preset.isEnabled()) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED;

        return preset.createIcon(imageState, 16);
    }

    protected Icon createIcon(MNetDevice device)
    {
        ClimateTrayImageState imageState =
            (device.isEnabled()) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED;

        return device.getState().createIcon(imageState, 16);
    }

    @Override
    public void apply(ClimateTrayPreferences model)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void modified(MessageList messages)
    {
        // TODO Auto-generated method stub

    }

    public void consume()
    {
        Point location = MouseInfo.getPointerInfo().getLocation();

        consume(location.x, location.y);
    }

    public void consume(int x, int y)
    {
        if (view.isVisible())
        {
            view.setVisible(false);
        }

        monitor.block();

        try
        {
            prepare(ClimateTray.PREFERENCES);
        }
        finally
        {
            monitor.unblock();
        }

        hiddenDialogForFocusManagement = new JDialog((Frame) null, "Climate Tray");

        hiddenDialogForFocusManagement.setUndecorated(true);
        hiddenDialogForFocusManagement.setIconImages(ClimateTrayImage.ICON.getImages(ClimateTrayImageState.NONE, 64,
            48, 32, 24, 16));
        hiddenDialogForFocusManagement.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        hiddenDialogForFocusManagement.setVisible(true);

        view.pack();
        view.show(hiddenDialogForFocusManagement, x, y);
    }

    public void presetSelect(MNetPreset preset)
    {
        ClimateTray.togglePreset(preset.getId());
    }

    public void deviceSelect(MNetDevice device)
    {
        ClimateTray.toggleDevice(device.getId());
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e)
    {
        // intentionally left blank
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
    {
        if (hiddenDialogForFocusManagement != null)
        {
            hiddenDialogForFocusManagement.setVisible(false);
            hiddenDialogForFocusManagement.dispose();

            hiddenDialogForFocusManagement = null;
        }
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e)
    {
        // intentionally left blank
    }

}
