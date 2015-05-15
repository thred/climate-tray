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
package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.MessageList;
import io.github.thred.climatetray.util.Severity;
import io.github.thred.climatetray.util.swing.BorderPanel;
import io.github.thred.climatetray.util.swing.ButtonPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;
import io.github.thred.climatetray.util.swing.TitlePanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

public abstract class AbstractClimateTrayDialogController<MODEL_TYPE, CONTROLLER_TYPE extends AbstractClimateTrayController<MODEL_TYPE, ? extends JComponent>>
    extends AbstractClimateTrayController<MODEL_TYPE, JDialog>
{

    public static final int BUTTON_CLOSE = 1 << 0;
    public static final int BUTTON_OK = 1 << 1;
    public static final int BUTTON_CANCEL = 1 << 2;
    public static final int BUTTON_OK_CANCEL = BUTTON_OK | BUTTON_CANCEL;

    private final CONTROLLER_TYPE controller;

    protected final TitlePanel titlePanel = new TitlePanel(null, null);

    protected final JButton okButton = SwingUtils.createButton("Ok", (e) -> ok());
    protected final JButton cancelButton = SwingUtils.createButton("Cancel", (e) -> cancel());
    protected final JButton closeButton = SwingUtils.createButton("Close", (e) -> close());
    protected final JDialog view;

    private MODEL_TYPE model;
    private boolean result;

    public AbstractClimateTrayDialogController(CONTROLLER_TYPE controller, int buttons)
    {
        super();

        this.controller = monitor(controller);

        view = new JDialog((Window) null, ModalityType.APPLICATION_MODAL);

        view.setIconImages(ClimateTrayImage.ICON.getImages(ClimateTrayImageState.NONE, 64, 48, 32, 24, 16));
        view.setLayout(new BorderLayout());
        view.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        view.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                close();
            }
        });

        view.add(titlePanel, BorderLayout.NORTH);
        view.add(createContentPanel(), BorderLayout.CENTER);
        view.add(createBottomPanel(buttons), BorderLayout.SOUTH);

        monitor.addMonitorListener((e) -> {
            modified();
        });
    }

    public void setTitle(String title)
    {
        view.setTitle(title);
        titlePanel.setTitle(title);
    }

    public void setTitle(Icon icon, String title)
    {
        view.setTitle(title);
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

    public void setDescription(MessageList messages)
    {
        setDescription(messages.size() > 0 ? messages.get(0) : null);
    }

    protected JComponent createContentPanel()
    {
        JScrollPane scrollPane = new JScrollPane(new BorderPanel(controller.getView()));

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    protected JComponent createBottomPanel(int buttons)
    {
        ButtonPanel buttonPanel = new ButtonPanel();

        if ((buttons & BUTTON_CLOSE) != 0)
        {
            buttonPanel.center(closeButton);
        }

        if ((buttons & BUTTON_OK) != 0)
        {
            buttonPanel.right(closeButton);
        }

        if ((buttons & BUTTON_CANCEL) != 0)
        {
            buttonPanel.right(closeButton);
        }

        return buttonPanel;
    }

    @Override
    public JDialog getView()
    {
        return view;
    }

    @Override
    public void prepare(MODEL_TYPE model)
    {
        controller.prepare(model);
    }

    @Override
    public void apply(MODEL_TYPE model)
    {
        controller.apply(model);
    }

    public void modified()
    {
        MessageList messages = new MessageList();

        modified(messages);

        messages.sortBySeverity();

        okButton.setEnabled(!messages.containsAtLeast(Severity.ERROR));

        setDescription(messages);
    }

    @Override
    public void modified(MessageList messages)
    {
        controller.modified(messages);
    }

    public boolean consume(Component parent, MODEL_TYPE model)
    {
        this.model = model;

        result = false;

        prepare(model);

        JDialog view = getView();

        view.pack();
        view.setLocationRelativeTo(parent);
        view.setVisible(true);

        return result;
    }

    public void ok()
    {
        apply(model);
        success(model);

        getView().setVisible(false);

        result = true;
    }

    protected void success(MODEL_TYPE model)
    {
        // intentionally left blank
    }

    public void close()
    {
        cancel();
    }

    public void cancel()
    {
        getView().setVisible(false);

        result = false;
    }

}
