/*
 * Copyright 2015 Manfred Hantschel
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
package io.github.thred.climatetray.ui;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.Severity;
import io.github.thred.climatetray.util.swing.BorderPanel;
import io.github.thred.climatetray.util.swing.ButtonPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;
import io.github.thred.climatetray.util.swing.TitlePanel;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public abstract class AbstractClimateTrayWindowController<MODEL_TYPE, VIEW_TYPE extends Window> extends
    AbstractClimateTrayController<MODEL_TYPE, VIEW_TYPE>
{

    public enum Button
    {
        YES,
        NO,
        OK,
        CANCEL,
        CLOSE
    }

    protected final Window owner;
    protected final AbstractClimateTrayController<MODEL_TYPE, ? extends JComponent> controller;
    protected final Button[] buttons;

    protected final TitlePanel titlePanel = new TitlePanel(null, null);
    protected final JButton yesButton = SwingUtils.createButton("Yes", (e) -> yes());
    protected final JButton noButton = SwingUtils.createButton("No", (e) -> no());
    protected final JButton okButton = SwingUtils.createButton("Ok", (e) -> ok());
    protected final JButton cancelButton = SwingUtils.createButton("Cancel", (e) -> cancel());
    protected final JButton closeButton = SwingUtils.createButton("Close", (e) -> close());

    private MODEL_TYPE model;
    private Button result;

    public AbstractClimateTrayWindowController(Window owner,
        AbstractClimateTrayController<MODEL_TYPE, ? extends JComponent> controller, Button... buttons)
    {
        super();

        this.owner = owner;
        this.controller = monitor(controller);
        this.buttons = buttons;

        monitor.addMonitorListener((e) -> {
            modified();
        });
    }

    @Override
    protected VIEW_TYPE createView()
    {
        VIEW_TYPE view = createWindow();

        view.setIconImages(ClimateTrayImage.ICON.getImages(ClimateTrayImageState.NONE, 64, 48, 32, 24, 16));
        view.setLayout(new BorderLayout());
        view.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                close();
            }
        });

        view.add(titlePanel, BorderLayout.NORTH);
        view.add(createContentPanel(controller.getView()), BorderLayout.CENTER);
        view.add(createBottomPanel(buttons), BorderLayout.SOUTH);

        return view;
    }

    protected abstract VIEW_TYPE createWindow();

    public void setTitle(String title)
    {
        titlePanel.setTitle(title);
    }

    public void setTitle(Icon icon, String title)
    {
        titlePanel.setTitle(icon, title);
    }

    public void setDescription(String description)
    {
        titlePanel.setDescription(description);
    }

    public void setDescription(Icon icon, String description)
    {
        titlePanel.setDescription(icon, description);
    }

    public void setDescription(Message message)
    {
        if (message != null)
        {
            setDescription(message.getSeverity().getImage().getIcon(ClimateTrayImageState.NONE, 16),
                message.getMessage());
        }
        else
        {
            setDescription(null, null);
        }
    }

    public void setDescription(MessageBuffer messageBuffer)
    {
        setDescription(messageBuffer.worst());
    }

    protected JComponent createContentPanel(JComponent contentView)
    {
        JScrollPane scrollPane = new JScrollPane(new BorderPanel(contentView));

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    protected JComponent createBottomPanel(Button... buttons)
    {
        Set<Button> set = new HashSet<>(Arrays.asList(buttons));
        ButtonPanel buttonPanel = new ButtonPanel();

        if (set.contains(Button.YES))
        {
            buttonPanel.right(yesButton);
        }

        if (set.contains(Button.NO))
        {
            buttonPanel.right(noButton);
        }

        if (set.contains(Button.OK))
        {
            buttonPanel.right(okButton);
        }

        if (set.contains(Button.CANCEL))
        {
            buttonPanel.right(cancelButton);
        }

        if (set.contains(Button.CLOSE))
        {
            buttonPanel.right(closeButton);
        }

        return buttonPanel;
    }

    @Override
    public void prepareWith(MODEL_TYPE model)
    {
        controller.prepareWith(model);
    }

    @Override
    public void applyTo(MODEL_TYPE model)
    {
        controller.applyTo(model);
    }

    public void modified()
    {
        MessageBuffer messageBuffer = new MessageBuffer();

        modified(messageBuffer);

        messageBuffer.sortBySeverity();

        okButton.setEnabled(!messageBuffer.containsAtLeast(Severity.ERROR));

        setDescription(messageBuffer);
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        controller.modified(messageBuffer);
    }

    @Override
    public void dismiss(MODEL_TYPE model)
    {
        getView().setVisible(false);

        controller.dismiss(model);

        this.model = null;
    }

    public Button consume(MODEL_TYPE model)
    {
        VIEW_TYPE view = getView();

        if (view.isVisible())
        {
            view.toFront();

            return result;
        }

        this.model = model;

        result = null;

        prepareWith(model);

        view.pack();
        view.setLocationRelativeTo(owner);
        view.revalidate();
        view.setVisible(true);

        return result;
    }

    public MODEL_TYPE getModel()
    {
        return model;
    }

    public void yes()
    {
        applyTo(model);

        result = Button.OK;

        dismiss(model);
    }

    public void no()
    {
        result = Button.CLOSE;

        dismiss(model);
    }

    public void ok()
    {
        applyTo(model);

        result = Button.OK;

        dismiss(model);
    }

    public void close()
    {
        result = Button.CLOSE;

        dismiss(model);
    }

    public void cancel()
    {
        result = Button.CANCEL;

        dismiss(model);
    }

}
