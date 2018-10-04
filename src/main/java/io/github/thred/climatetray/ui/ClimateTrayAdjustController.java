package io.github.thred.climatetray.ui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import io.github.thred.climatetray.mnet.MNetAdjust;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.ui.MNetAdjustController;
import io.github.thred.climatetray.util.swing.GBC;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class ClimateTrayAdjustController extends AbstractClimateTrayController<MNetAdjust, JComponent>
{

    private final MNetAdjustController adjustController = new MNetAdjustController();
    private final ClimateTrayDeviceSelectController deviceController = monitor(new ClimateTrayDeviceSelectController());
    private final ClimateTrayPresetSelectController presetController = monitor(new ClimateTrayPresetSelectController());

    protected final EventListenerList listenerList = new EventListenerList();

    private final JPanel devicePanel = SwingUtils.createPanel(new BorderLayout(0, 8));
    private final JPanel presetPanel = SwingUtils.createPanel(new BorderLayout(0, 8));

    public ClimateTrayAdjustController()
    {
        super();

        adjustController.addMonitorListener(e -> {
            MNetPreset preset = new MNetPreset();
            
            adjustController.applyTo(preset);
            presetController.setSelectedValue(presetController.listModel.getList().stream().filter($ -> $.isSame(preset)).findFirst().orElse(null));
        });
    }

    public ClimateTrayDeviceSelectController getDeviceController()
    {
        return deviceController;
    }

    public ClimateTrayPresetSelectController getPresetController()
    {
        return presetController;
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

    @Override
    protected JComponent createView()
    {
        JPanel view = new JPanel(new BorderLayout());

        view.setOpaque(false);

        JPanel sidePanel = new JPanel(new GridBagLayout());

        sidePanel.setOpaque(false);

        devicePanel.add(deviceController.getView(), BorderLayout.CENTER);
        presetPanel.add(presetController.getView(), BorderLayout.CENTER);

        GBC gbc = new GBC(1, 2);

        sidePanel.add(devicePanel, gbc.fill().weight(1, 1));
        sidePanel.add(presetPanel, gbc.next().fill().weight(1, 1));

        view.add(sidePanel, BorderLayout.CENTER);
        view.add(adjustController.getView(), BorderLayout.EAST);

        return view;
    }

    @Override
    public void prepareWith(MNetAdjust model)
    {
        deviceController.prepareWith(model.getDevices());
        presetController.prepareWith(model.getPresets());
        adjustController.prepareWith(model.getPreset());

        super.prepareWith(model);
    }

    @Override
    public void refreshWith(MNetAdjust model)
    {
        if (!model.isAnyDeviceSelected())
        {
            model.selectFirstDevice();
        }

        devicePanel.setVisible(model.countEnabledDevices() > 1);
        presetPanel.setVisible(model.isAnyPresetAvailable());

        deviceController.refreshWith(model.getDevices());
        presetController.refreshWith(model.getPresets());
        adjustController.refreshWith(model.getPreset());
    }

    @Override
    public void applyTo(MNetAdjust model)
    {
        deviceController.applyTo(model.getDevices());
        presetController.applyTo(model.getPresets());
        adjustController.applyTo(model.getPreset());
    }

    @Override
    public void dismiss(MNetAdjust model)
    {
        deviceController.dismiss(model.getDevices());
        presetController.dismiss(model.getPresets());
        adjustController.dismiss(model.getPreset());
    }

}
