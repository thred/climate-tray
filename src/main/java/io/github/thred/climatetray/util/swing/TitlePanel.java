/*
 * Copyright 2015, 2016 Manfred Hantschel
 *
 * This file is part of Climate-Tray.
 *
 * Climate-Tray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * Climate-Tray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Climate-Tray. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package io.github.thred.climatetray.util.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.github.thred.climatetray.ClimateTrayCache;

public class TitlePanel extends JPanel
{

    private static final long serialVersionUID = 7153479579609032115L;

    private final Image backgroundImage = ClimateTrayCache.getImage("background-dialog.png");

    private final JLabel titleLabel = new JLabel("");
    private final JLabel descriptionLabel = new JLabel("");

    public TitlePanel(String title, String description)
    {
        super(new BorderLayout());

        setPreferredSize(new Dimension(320, 64));

        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getSize() * 2f));
        titleLabel.setForeground(new Color(0x191970));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 16, 2, 80));

        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(2, 16, 4, 80));

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
