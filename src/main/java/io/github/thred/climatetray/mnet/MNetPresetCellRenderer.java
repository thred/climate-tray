package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTrayImageState;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class MNetPresetCellRenderer extends DefaultListCellRenderer
{

    private static final long serialVersionUID = 8417832942032540610L;

    private static final int ICON_SIZE = 24;

    public MNetPresetCellRenderer()
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

        MNetPreset preset = (MNetPreset) value;
        ClimateTrayImageState imageState =
            (isSelected) ? ClimateTrayImageState.SELECTED : ClimateTrayImageState.NOT_SELECTED;

        setIcon(preset.createIcon(imageState, ICON_SIZE));
        setText(preset.describe());

        return this;
    }

}
