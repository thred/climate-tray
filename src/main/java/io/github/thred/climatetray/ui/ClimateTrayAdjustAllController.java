package io.github.thred.climatetray.ui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.ui.MNetAdjustController;
import io.github.thred.climatetray.util.swing.GBC;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class ClimateTrayAdjustAllController extends AbstractClimateTrayController<MNetPreset, JComponent>
{

    private final MNetAdjustController adjustController = new MNetAdjustController();
    private final ClimateTrayDeviceSelectController deviceController = monitor(new ClimateTrayDeviceSelectController());
    private final ClimateTrayPresetSelectController presetController = monitor(new ClimateTrayPresetSelectController());

    protected final EventListenerList listenerList = new EventListenerList();

    public ClimateTrayAdjustAllController()
    {
        super();

        adjustController.addMonitorListener(e -> {
            presetController.clearSelection();
        });

        presetController.addSelectListener(e -> fireSelectEvent(e));
    }

    public void addActionListener(ActionListener listener)
    {
        listenerList.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener)
    {
        listenerList.remove(ActionListener.class, listener);
    }

    protected void fireActionEvent(ActionEvent event)
    {
        for (ActionListener listener : listenerList.getListeners(ActionListener.class))
        {
            listener.actionPerformed(event);
        }
    }

    public void addSelectListener(SelectListener<MNetPreset> listener)
    {
        listenerList.add(SelectListener.class, listener);
    }

    public void removeSelectListener(SelectListener<MNetPreset> listener)
    {
        listenerList.remove(SelectListener.class, listener);
    }

    @SuppressWarnings("unchecked")
    protected void fireSelectEvent(SelectEvent<MNetPreset> event)
    {
        for (SelectListener<MNetPreset> listener : listenerList.getListeners(SelectListener.class))
        {
            listener.itemSelected(event);
        }
    }

    @Override
    protected JComponent createView()
    {
        JPanel view = new JPanel(new BorderLayout());

        view.setOpaque(false);

        JPanel sidePanel = new JPanel(new GridBagLayout());

        sidePanel.setOpaque(false);

        GBC gbc = new GBC(1, 4);

        sidePanel.add(SwingUtils.createLabel("Devices (check to adjust):"), gbc);
        sidePanel.add(deviceController.getView(), gbc.next().fill().weight(1, 1));

        sidePanel.add(SwingUtils.createLabel("Presets:"), gbc.next());
        sidePanel.add(presetController.getView(), gbc.next().fill().weight(1, 1));

        view.add(sidePanel, BorderLayout.CENTER);
        view.add(adjustController.getView(), BorderLayout.EAST);

        return view;
    }

    @Override
    public void prepareWith(MNetPreset model)
    {
        deviceController.prepareWith(ClimateTray.PREFERENCES.getDevices());
        presetController.prepareWith(ClimateTray.PREFERENCES.getPresets());
        adjustController.prepareWith(model);

        super.prepareWith(model);
    }

    @Override
    public void refreshWith(MNetPreset model)
    {
        deviceController.refreshWith(ClimateTray.PREFERENCES.getDevices());
        presetController.refreshWith(ClimateTray.PREFERENCES.getPresets());
        adjustController.prepareWith(model);
    }

    @Override
    public void applyTo(MNetPreset model)
    {
        deviceController.applyTo(ClimateTray.PREFERENCES.getDevices());
        presetController.applyTo(ClimateTray.PREFERENCES.getPresets());
        adjustController.applyTo(model);
    }

    @Override
    public void dismiss(MNetPreset model)
    {
        deviceController.dismiss(ClimateTray.PREFERENCES.getDevices());
        presetController.dismiss(ClimateTray.PREFERENCES.getPresets());
        adjustController.dismiss(model);
    }

}
