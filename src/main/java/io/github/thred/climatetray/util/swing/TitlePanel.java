package io.github.thred.climatetray.util.swing;

import io.github.thred.climatetray.ClimateTrayCache;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitlePanel extends JPanel
{

    private static final long serialVersionUID = 7153479579609032115L;

    private final Image backgroundImage = ClimateTrayCache.getImage("background-dialog.png");

    private final JLabel titleLabel = new JLabel("");
    private final JLabel descriptionLabel = new JLabel("");

    public TitlePanel(String title, String description)
    {
        super(new BorderLayout());

        setPreferredSize(new Dimension(256, 64));

        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2f));
        titleLabel.setForeground(new Color(0x191970));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 16, 2, 16));

        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(2, 16, 4, 16));

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        add(titleLabel, BorderLayout.NORTH);
        add(descriptionLabel, BorderLayout.CENTER);

        setTitle(title);
        setDescription(description);
    }

    public String getTitle()
    {
        return titleLabel.getText();
    }

    public void setTitle(String title)
    {
        titleLabel.setText((title != null) ? title : "");
    }

    public Icon getTitleIcon()
    {
        return titleLabel.getIcon();
    }

    public void setTitle(Icon icon, String title)
    {
        setTitleIcon(icon);
        setTitle(title);
    }

    public void setTitleIcon(Icon icon)
    {
        titleLabel.setIcon(icon);
    }

    public String getDescription()
    {
        return descriptionLabel.getText();
    }

    public void setDescription(String description)
    {
        descriptionLabel.setText((description != null) ? description : "");
    }

    public void setDescription(Icon icon, String description)
    {
        setDescriptionIcon(icon);
        setDescription(description);
    }

    public Icon getDescriptionIcon()
    {
        return descriptionLabel.getIcon();
    }

    public void setDescriptionIcon(Icon icon)
    {
        descriptionLabel.setIcon(icon);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.drawImage(backgroundImage, getWidth() - backgroundImage.getWidth(this), 0, null);
    }
}
