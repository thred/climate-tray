package io.github.thred.climatetray.controller;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.TemperatureUnit;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class ClimateTrayPreferencesController extends AbstractClimateTrayController<ClimateTrayPreferences, JComponent>
{

    private final ClimateTrayPresetListController presetListController = monitor(new ClimateTrayPresetListController());
    private final ClimateTrayDeviceListController deviceListController = monitor(new ClimateTrayDeviceListController());

    private final JComboBox<TemperatureUnit> temperatureUnitBox = monitor(createComboBox(TemperatureUnit.values()));

    private final JPanel view = new JPanel(new GridBagLayout());

    public ClimateTrayPreferencesController()
    {
        super();

        temperatureUnitBox.addActionListener((e) -> refresh());

        JPanel presetListView = presetListController.getView();
        JPanel deviceListView = deviceListController.getView();

        GBC gbc = new GBC(2, 6);

        view.add(createLabel("Unit of Temperature:", temperatureUnitBox), gbc);
        view.add(temperatureUnitBox, gbc.next());

        view.add(createLabel("Presets:", presetListView), gbc.next().top().insetTop(8));
        view.add(presetListView, gbc.next().weight(1, 1).fill());

        view.add(createLabel("Devices:", deviceListView), gbc.next().top().insetTop(8));
        view.add(deviceListView, gbc.next().weight(1, 1).fill());
    }

    @Override
    public JComponent getView()
    {
        return view;
    }

    @Override
    public void prepare(ClimateTrayPreferences model)
    {
        temperatureUnitBox.setSelectedItem(model.getTemperatureUnit());

        presetListController.prepare(model.getPresets());
        deviceListController.prepare(model.getDevices());
    }

    @Override
    public void apply(ClimateTrayPreferences model)
    {
        model.setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());

        presetListController.apply(model.getPresets());
        deviceListController.apply(model.getDevices());
    }

    @Override
    public void modified(MessageList messages)
    {
        presetListController.modified(messages);
        deviceListController.modified(messages);
    }

    public void refresh()
    {
        ClimateTray.PREFERENCES.setTemperatureUnit((TemperatureUnit) temperatureUnitBox.getSelectedItem());

        presetListController.refresh();
        deviceListController.refresh();
    }

}
