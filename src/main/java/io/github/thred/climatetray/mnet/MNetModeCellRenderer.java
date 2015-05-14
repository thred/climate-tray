package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImageState;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MNetModeCellRenderer extends DefaultListCellRenderer
{

    private static final long serialVersionUID = -1286708018290981191L;

    public MNetModeCellRenderer()
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

        MNetMode mode = (MNetMode) value;

        setIcon(mode.getImage().getIcon((isSelected) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED, 16));
        setText(mode.getLabel());

        return this;
    }
}
