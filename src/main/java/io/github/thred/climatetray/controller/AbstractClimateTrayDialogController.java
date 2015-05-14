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

    private final CONTROLLER_TYPE controller;

    protected final TitlePanel titlePanel = new TitlePanel(null, null);

    protected final JButton okButton = SwingUtils.createButton("Ok", (e) -> ok());
    protected final JButton cancelButton = SwingUtils.createButton("Cancel", (e) -> cancel());
    protected final JDialog view;

    private MODEL_TYPE model;
    private boolean result;

    public AbstractClimateTrayDialogController(CONTROLLER_TYPE controller)
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
        view.add(createBottomPanel(), BorderLayout.SOUTH);

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

    protected JComponent createBottomPanel()
    {
        return new ButtonPanel().right(okButton, cancelButton);
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
