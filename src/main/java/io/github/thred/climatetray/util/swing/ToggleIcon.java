package io.github.thred.climatetray.util.swing;

import javax.swing.Icon;
import javax.swing.JButton;

public class ToggleIcon extends JButton
{

    private static final long serialVersionUID = -6808770713457871071L;

    private Icon onIcon;
    private Icon offIcon;

    private boolean selected = false;

    public ToggleIcon()
    {
        super();
    }

    public ToggleIcon(Icon onIcon, Icon offIcon)
    {
        super(offIcon);

        this.onIcon = onIcon;
        this.offIcon = offIcon;
    }

    @Override
    public boolean isSelected()
    {
        return selected;
    }

    @Override
    public void setSelected(boolean selected)
    {
        if (this.selected != selected)
        {
            this.selected = selected;

            setIcon(selected ? onIcon : offIcon);
        }
    }
}
