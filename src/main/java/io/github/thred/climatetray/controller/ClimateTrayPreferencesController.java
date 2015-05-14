package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.TemperatureUnit;
import io.github.thred.climatetray.util.swing.GBC;
import io.github.thred.climatetray.util.swing.SwingUtils;

import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ClimateTrayPreferencesController extends AbstractClimateTrayController<ClimateTrayPreferences, JComponent>
{

    private final ClimateTrayPresetListController presetListController = new ClimateTrayPresetListController();
    private final ClimateTrayDeviceListController deviceListController = new ClimateTrayDeviceListController();

    private final JComboBox<TemperatureUnit> temperatureUnitBox = monitor(SwingUtils.createComboBox(TemperatureUnit
        .values()));

    public ClimateTrayPreferencesController()
    {
        super();

        temperatureUnitBox.addActionListener((e) -> temperatureUnitUpdated());
    }

    @Override
    protected JComponent createView()
    {
        JPanel presetListView = presetListController.getView();
        JPanel deviceListView = deviceListController.getView();

        JPanel panel = new JPanel(new GridBagLayout());
        GBC gbc = new GBC(2, 6);

        panel.add(SwingUtils.createLabel("Unit of Temperature:", temperatureUnitBox), gbc);
        panel.add(temperatureUnitBox, gbc.next());

        panel.add(SwingUtils.createLabel("Presets:", presetListView), gbc.next().top().insetTop(8));
        panel.add(presetListView, gbc.next().weight(1, 1).fill());

        panel.add(SwingUtils.createLabel("Devices:", deviceListView), gbc.next().top().insetTop(8));
        panel.add(deviceListView, gbc.next().weight(1, 1).fill());

        return panel;
    }

    @Override
    protected void localPrepare(ClimateTrayPreferences model)
    {
        temperatureUnitBox.setSelectedItem(model.getTemperatureUnit());

        presetListController.prepare(model.getPresets());
        deviceListController.prepare(model.getDevices());
    }

    @Override
    protected void localApply(ClimateTrayPreferences model)
    {
        model.setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());

        presetListController.localApply(model.getPresets());
        deviceListController.localApply(model.getDevices());
    }

    @Override
    protected void localCheck(MessageList messages)
    {
        messages.addAll(presetListController.check());
        messages.addAll(deviceListController.check());
    }

    public void temperatureUnitUpdated()
    {
        getModel().setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());

        presetListController.refreshView();
        deviceListController.refreshView();
    }

}
