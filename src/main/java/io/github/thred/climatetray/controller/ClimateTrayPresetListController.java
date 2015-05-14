package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.MNetPresetCellRenderer;
import io.github.thred.climatetray.util.swing.AdvancedListModel;

import javax.swing.JList;

public class ClimateTrayPresetListController extends AbstractClimateTrayListController<MNetPreset>
{

    public ClimateTrayPresetListController()
    {
        super();
    }

    @Override
    protected JList<MNetPreset> createList(AdvancedListModel<MNetPreset> listModel)
    {
        JList<MNetPreset> list = super.createList(listModel);

        list.setCellRenderer(new MNetPresetCellRenderer());

        return list;
    }

    @Override
    protected MNetPreset createInstance()
    {
        return new MNetPreset();
    }

    @Override
    protected boolean edit(MNetPreset preset)
    {
        return new ClimateTrayPresetDialogController().consume(getView(), preset);
    }

}
