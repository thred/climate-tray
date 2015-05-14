package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.controller.AbstractClimateTrayController;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.TemperatureUnit;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MNetPresetController extends AbstractClimateTrayController<MNetPreset, JPanel>
{

    private static final double DEFAULT = 22;
    private static final double MINIMUM = 17;
    private static final double MAXIMUM = 30;

    private final JComboBox<MNetMode> modeBox = monitor(createComboBox(MNetMode.values()));
    private final SpinnerNumberModel temperatureSpinnerModel = new SpinnerNumberModel(DEFAULT, MINIMUM, MAXIMUM, 0.5);
    private final JSpinner temperatureSpinner = monitor(createSpinner(temperatureSpinnerModel));
    private final JLabel temperatureLabel = createLabel("Temperature:", temperatureSpinner);
    private final JComboBox<MNetFan> fanBox = monitor(createComboBox(MNetFan.values()));
    private final JComboBox<MNetAir> airBox = monitor(createComboBox(MNetAir.values()));
    private final JPanel view = new JPanel(new GridBagLayout());

    private JSpinner.NumberEditor temperatureSpinnerEditor = new JSpinner.NumberEditor(temperatureSpinner, "0.0");

    public MNetPresetController()
    {
        super();

        modeBox.setRenderer(new MNetModeCellRenderer());

        temperatureSpinner.setEditor(temperatureSpinnerEditor);

        fanBox.setRenderer(new MNetFanCellRenderer());
        airBox.setRenderer(new MNetAirCellRenderer());

        GBC gbc = new GBC(2, 4);

        view.add(createLabel("Mode:", modeBox), gbc);
        view.add(modeBox, gbc.next().hFill());

        view.add(temperatureLabel, gbc.next());
        view.add(temperatureSpinner, gbc.next().hFill());

        view.add(createLabel("Fan:", fanBox), gbc.next());
        view.add(fanBox, gbc.next().hFill());

        view.add(createLabel("Air:", airBox), gbc.next());
        view.add(airBox, gbc.next().hFill());
    }

    @Override
    public JPanel getView()
    {
        return view;
    }

    @Override
    public void prepare(MNetPreset model)
    {
        TemperatureUnit temperatureUnit = ClimateTray.PREFERENCES.getTemperatureUnit();

        temperatureLabel.setText(String.format("Temperature (in %s):", temperatureUnit.getSymbol()));

        temperatureSpinnerModel.setMinimum(temperatureUnit.convertFromCelsius(MINIMUM));
        temperatureSpinnerModel.setMaximum(temperatureUnit.convertFromCelsius(MAXIMUM));
        temperatureSpinnerModel.setStepSize(temperatureUnit.getStep());

        temperatureSpinnerEditor = new JSpinner.NumberEditor(temperatureSpinner, temperatureUnit.getNumberFormat());

        temperatureSpinner.setEditor(temperatureSpinnerEditor);
        temperatureSpinner.setValue(temperatureUnit.convertFromCelsius(model.getTemperature()));

        modeBox.setSelectedItem(model.getMode());
        fanBox.setSelectedItem(model.getFan());
        airBox.setSelectedItem(model.getAir());
    }

    @Override
    public void apply(MNetPreset model)
    {
        TemperatureUnit temperatureUnit = ClimateTray.PREFERENCES.getTemperatureUnit();

        model.setTemperature(temperatureUnit.convertToCelsius((Double) temperatureSpinner.getValue()));

        model.setMode((MNetMode) modeBox.getSelectedItem());
        model.setFan((MNetFan) fanBox.getSelectedItem());
        model.setAir((MNetAir) airBox.getSelectedItem());
    }

    @Override
    public void modified(MessageList messages)
    {
        MNetMode mode = (MNetMode) modeBox.getSelectedItem();

        temperatureSpinner.setEnabled(mode.isTemperatureEnabled());
        fanBox.setEnabled(mode.isFanEnabled());
        airBox.setEnabled(mode.isAirEnabled());
    }
}
