package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.swing.GBC;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ClimateTrayPreferencesController extends AbstractClimateTrayController<ClimateTrayPreferences, JComponent>
{

    private final ClimateTrayPresetListController presetListController = new ClimateTrayPresetListController();
    private final ClimateTrayDeviceListController deviceListController = new ClimateTrayDeviceListController();

    public ClimateTrayPreferencesController()
    {
        super();
    }

    @Override
    protected JComponent createView()
    {
        JPanel presetListView = presetListController.getView();
        JPanel deviceListView = deviceListController.getView();

        JPanel panel = new JPanel(new GridBagLayout());
        GBC gbc = new GBC(2, 6);

        panel.add(SwingUtils.createLabel("Presets:", presetListView), gbc.top().insetTop(12));
        panel.add(presetListView, gbc.next().weight(1, 1).fill());

        panel.add(SwingUtils.createLabel("Devices:", deviceListView), gbc.next().top().insetTop(12));
        panel.add(deviceListView, gbc.next().weight(1, 1).fill());

        return panel;
    }

    @Override
    protected void localPrepare(ClimateTrayPreferences model)
    {
        presetListController.prepare(model.getPresets());
        deviceListController.prepare(model.getDevices());
    }

    @Override
    protected void localApply(ClimateTrayPreferences model)
    {
        presetListController.localApply(model.getPresets());
        deviceListController.localApply(model.getDevices());
    }

    @Override
    protected void localCheck(MessageList messages)
    {
        messages.addAll(presetListController.check());
        messages.addAll(deviceListController.check());
    }

}
