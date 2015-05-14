package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.util.MessageList;
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

    private boolean result;

    public AbstractClimateTrayDialogController(CONTROLLER_TYPE controller)
    {
        super();

        this.controller = controller;

        monitor(controller.getMonitor());
    }

    @Override
    protected final JDialog createView()
    {
        String title = getTitle();

        titlePanel.setTitle(title);
        // titlePanel.setTitleIcon(ClimateTrayImage.ICON.getIcon(ClimateTrayImageState.NONE, 32));

        JDialog view = new JDialog((Window) null, title, ModalityType.APPLICATION_MODAL);

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

        return view;
    }

    protected abstract String getTitle();

    @Override
    protected void localPrepare(MODEL_TYPE model)
    {
        controller.prepare(model);
    }

    @Override
    public void localApply(MODEL_TYPE model)
    {
        controller.apply();
    }

    @Override
    protected void localCheck(MessageList messages)
    {
        messages.addAll(controller.check());
    }

    protected JComponent createContentPanel()
    {
        JScrollPane scrollPane = new JScrollPane(new BorderPanel(controller.createView()));

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    protected JComponent createBottomPanel()
    {
        return new ButtonPanel().right(okButton, cancelButton);
    }

    public boolean consume(Component parent, MODEL_TYPE model)
    {
        result = false;

        prepare(model);

        JDialog view = getView();

        view.pack();
        view.setLocationRelativeTo(parent);
        view.setVisible(true);

        return result;
    }

    @Override
    protected void checked(boolean valid, MessageList messages)
    {
        okButton.setEnabled(valid);
    }

    public void ok()
    {
        MODEL_TYPE model = getModel();

        localApply(model);

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
