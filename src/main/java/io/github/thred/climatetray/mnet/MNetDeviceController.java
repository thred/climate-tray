package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.controller.AbstractClimateTrayController;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class MNetDeviceController extends AbstractClimateTrayController<MNetDevice, JPanel>
{

    private final JTextField nameField = monitor(createTextField("", 32));
    private final JTextField hostField = monitor(createTextField("", 32));
    private final JSpinner addressField = monitor(createSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1)));

    public MNetDeviceController()
    {
        super();
    }

    @Override
    protected JPanel createView()
    {
        JPanel view = new JPanel(new GridBagLayout());
        GBC gbc = new GBC(3, 4);

        view.add(createLabel("Name:", nameField), gbc);
        view.add(nameField, gbc.next().span(2).hFill());

        view.add(createLabel("Host:", hostField), gbc.next());
        view.add(hostField, gbc.next().span(2).hFill());

        view.add(createLabel("Address:", addressField), gbc.next());
        view.add(addressField, gbc.next().hFill());

        return view;
    }

    @Override
    protected void localPrepare(MNetDevice model)
    {
        nameField.setText(Utils.ensure(model.getName(), ""));
        hostField.setText(Utils.ensure(model.getHost(), ""));
        addressField.setValue(Utils.ensure(model.getAddress(), 0));
    }

    @Override
    protected void localApply(MNetDevice model)
    {
        model.setName(nameField.getText().trim());
        model.setHost(hostField.getText().trim());
        model.setAddress((Integer) addressField.getValue());
    }

    @Override
    protected void localCheck(MessageList messages)
    {
        String name = nameField.getText().trim();

        if (name.length() <= 0)
        {
            messages.addError("The name is missing.");
        }

        String host = hostField.getText().trim();

        if (host.length() <= 0)
        {
            messages.addError("The host is missing.");
        }
    }

}
