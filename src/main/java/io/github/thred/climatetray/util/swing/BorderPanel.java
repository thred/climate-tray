package io.github.thred.climatetray.util.swing;

import java.awt.BorderLayout;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BorderPanel extends JPanel
{

    private static final Border DEFAULT_BORDER = BorderFactory.createEmptyBorder(8, 12, 8, 12);
    private static final long serialVersionUID = -3653014923804516104L;

    public BorderPanel(LayoutManager layoutManager)
    {
        this(layoutManager, DEFAULT_BORDER, null);
    }

    public BorderPanel(LayoutManager layoutManager, Border border)
    {
        this(layoutManager, border, null);
    }

    public BorderPanel(JComponent component)
    {
        this(new BorderLayout(), DEFAULT_BORDER, component);
    }

    public BorderPanel(Border border, JComponent component)
    {
        this(new BorderLayout(), border, component);
    }

    protected BorderPanel(LayoutManager layoutManager, Border border, JComponent component)
    {
        super(layoutManager);

        setOpaque(false);

        if (border != null)
        {
            setBorder(border);
        }

        if (component != null)
        {
            add(component, BorderLayout.CENTER);
        }
    }

}
