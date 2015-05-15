package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MNetDeviceCellRenderer extends DefaultListCellRenderer
{

    private static final int ICON_SIZE = 24;

    private static final long serialVersionUID = 4726568871394455004L;

    public MNetDeviceCellRenderer()
    {
        super();
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
        boolean cellHasFocus)
    {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (value == null)
        {
            setText("");

            return this;
        }

        MNetDevice device = (MNetDevice) value;
        MNetState state = device.getState();
        ClimateTrayImageState imageState =
            (isSelected) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED;

        setIcon((state != null) ? state.createIcon(imageState, ICON_SIZE) : ClimateTrayImage.ICON.getIcon(imageState,
            ICON_SIZE));
        setText(device.describe(true, false));

        return this;
    }
}
