package io.github.thred.climatetray.controller;

import static io.github.thred.climatetray.util.swing.SwingUtils.*;
import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.ClimateTrayImage;
import io.github.thred.climatetray.ClimateTrayImageState;
import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.MessageList;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.WindowConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ClimateTrayPopupController extends AbstractClimateTrayController<ClimateTrayPreferences, JPopupMenu>
    implements PopupMenuListener
{

    private final JMenuItem preferencesItem = createMenuItem("Preferences...", null,
        "Manage the presets, devices and other settings.");
    private final JMenuItem exitItem = createMenuItem("Exit", null, null);
    private final List<JRadioButtonMenuItem> presetItems = new ArrayList<>();

    private JDialog hiddenDialogForFocusManagement;

    public ClimateTrayPopupController()
    {
        super();

        preferencesItem.addActionListener((e) -> ClimateTray.preferences());
        exitItem.addActionListener((e) -> ClimateTray.exit());
    }

    @Override
    protected JPopupMenu createView()
    {
        JPopupMenu view = new JPopupMenu("Climate Tray");

        view.addPopupMenuListener(this);

        view.add(preferencesItem);
        view.add(exitItem);

        return view;
    }

    @Override
    protected void localPrepare(ClimateTrayPreferences model)
    {
        JPopupMenu view = getView();
        int index = 0;

        presetItems.stream().forEach((item) -> view.remove(item));
        
//        model.getPresets().forEach((preset) -> {
//           preset. 
//        });
        // TODO Auto-generated method stub

    }

    @Override
    protected void localApply(ClimateTrayPreferences model)
    {
        // TODO Auto-generated method stub

    }

    @Override
    protected void localCheck(MessageList messages)
    {
        // TODO Auto-generated method stub

    }

    public void consume(int x, int y)
    {
        JPopupMenu view = getView();

        if (view.isVisible())
        {
            view.setVisible(false);
        }

        prepare(ClimateTray.PREFERENCES);

        hiddenDialogForFocusManagement = new JDialog((Frame) null, "Climate Tray");

        hiddenDialogForFocusManagement.setUndecorated(true);
        hiddenDialogForFocusManagement.setIconImages(ClimateTrayImage.ICON.getImages(
            ClimateTrayImageState.NONE, 64, 48, 32, 24, 16));
        hiddenDialogForFocusManagement.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        hiddenDialogForFocusManagement.setVisible(true);

        view.pack();
        view.show(hiddenDialogForFocusManagement, x, y);
    }

    @Override
    public void popupMenuWillBecomeVisible(PopupMenuEvent e)
    {
        // intentionally left blank
    }

    @Override
    public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
    {
        if (hiddenDialogForFocusManagement != null)
        {
            hiddenDialogForFocusManagement.setVisible(false);
            hiddenDialogForFocusManagement.dispose();

            hiddenDialogForFocusManagement = null;
        }
    }

    @Override
    public void popupMenuCanceled(PopupMenuEvent e)
    {
        // intentionally left blank
    }

}
