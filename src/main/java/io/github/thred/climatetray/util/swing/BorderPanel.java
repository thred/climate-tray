package io.github.thred.climatetray.util.swing;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class BorderPanel extends JPanel
{

    private static final long serialVersionUID = -3653014923804516104L;

    public BorderPanel()
    {
        this(null);
    }

    public BorderPanel(JComponent component)
    {
        super(new BorderLayout());

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        if (component != null)
        {
            add(component, BorderLayout.CENTER);
        }
    }
}
