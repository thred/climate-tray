package io.github.thred.climatetray.util.message;

import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.swing.GBC;

import java.awt.GridBagLayout;
import java.awt.Window;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class MessageComponent extends JPanel
{

    private static final long serialVersionUID = -8346274765018474917L;

    private final JTextArea messageArea = new JTextArea();
    private final JLabel iconLabel = new JLabel();

    private Message message = null;
    private int iconSize = 48;

    public MessageComponent()
    {
        super();

        setLayout(new GridBagLayout());

        messageArea.setWrapStyleWord(true);
        messageArea.setLineWrap(true);
        messageArea.setOpaque(false);
        messageArea.setFont(iconLabel.getFont());
        messageArea.setEditable(false);
        messageArea.setRows(1);
        messageArea.setColumns(40);

        GBC gbc = new GBC(2, 1);

        add(iconLabel, gbc.top());
        add(messageArea, gbc.next().hFill().weight(1).center());
    }

    public MessageComponent(Message message)
    {
        this();

        setMessage(message);
    }

    public MessageComponent(int iconSize)
    {
        this();

        setIconSize(iconSize);
    }

    public MessageComponent(int iconSize, Message message)
    {
        this();

        setIconSize(iconSize);
        setMessage(message);
    }

    public Message getMessage()
    {
        return message;
    }

    public void setMessage(Message message)
    {
        this.message = message;

        messageArea.setText((message != null) ? message.getMessage() : "");
        iconLabel.setIcon((message != null) ? message.getSeverity().getImage()
            .getIcon(ClimateTrayImageState.NONE, iconSize) : null);

        Window window = SwingUtilities.getWindowAncestor(this);

        if (window != null)
        {
            SwingUtilities.invokeLater(() -> {
                window.pack();
                window.revalidate();
            });
        }
    }

    public int getIconSize()
    {
        return iconSize;
    }

    public void setIconSize(int iconSize)
    {
        this.iconSize = iconSize;
    }

    public JTextArea getMessageArea()
    {
        return messageArea;
    }

    public JLabel getIconLabel()
    {
        return iconLabel;
    }

}
