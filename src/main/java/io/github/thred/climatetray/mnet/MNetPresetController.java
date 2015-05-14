package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.controller.AbstractClimateTrayController;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class MNetPresetController extends AbstractClimateTrayController<MNetPreset, JPanel>
{

    private final JComboBox<MNetMode> modeBox = monitor(createComboBox(MNetMode.values()));
    private final JSpinner temperatureSpinner = monitor(createSpinner(new SpinnerNumberModel(22, 17, 30, 0.5)));
    private final JComboBox<MNetFan> fanBox = monitor(createComboBox(MNetFan.values()));
    private final JComboBox<MNetAir> airBox = monitor(createComboBox(MNetAir.values()));

    public MNetPresetController()
    {
        super();

        modeBox.setRenderer(new MNetModeCellRenderer());
        temperatureSpinner.setEditor(new JSpinner.NumberEditor(temperatureSpinner, "0.0"));
        fanBox.setRenderer(new MNetFanCellRenderer());
        airBox.setRenderer(new MNetAirCellRenderer());
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new GridBagLayout());
        GBC gbc = new GBC(2, 4);

        view.add(createLabel("Mode:", modeBox), gbc);
        view.add(modeBox, gbc.next().hFill());

        view.add(createLabel("Temperature:", temperatureSpinner), gbc.next());
        view.add(temperatureSpinner, gbc.next().hFill());

        view.add(createLabel("Fan:", fanBox), gbc.next());
        view.add(fanBox, gbc.next().hFill());

        view.add(createLabel("Air:", airBox), gbc.next());
        view.add(airBox, gbc.next().hFill());

        return view;
    }

    @Override
    protected void localPrepare(MNetPreset model)
    {
        modeBox.setSelectedItem(model.getMode());
        temperatureSpinner.setValue(model.getTemperature());
        fanBox.setSelectedItem(model.getFan());
        airBox.setSelectedItem(model.getAir());
    }

    @Override
    protected void localApply(MNetPreset model)
    {
        model.setMode((MNetMode) modeBox.getSelectedItem());
        model.setTemperature((Double) temperatureSpinner.getValue());
        model.setFan((MNetFan) fanBox.getSelectedItem());
        model.setAir((MNetAir) airBox.getSelectedItem());
    }

    @Override
    protected void localCheck(MessageList messages)
    {
        MNetMode mode = (MNetMode) modeBox.getSelectedItem();

        temperatureSpinner.setEnabled(mode.isTemperatureEnabled());
        fanBox.setEnabled(mode.isFanEnabled());
        airBox.setEnabled(mode.isAirEnabled());
    }
}
