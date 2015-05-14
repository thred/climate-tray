package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.mnet.MNetPresetCellRenderer;
import io.github.thred.climatetray.util.MessageList;

public class ClimateTrayPresetListController extends AbstractClimateTrayListController<MNetPreset>
{

    public ClimateTrayPresetListController()
    {
        super();

        list.setCellRenderer(new MNetPresetCellRenderer());
    }

    @Override
    protected MNetPreset createElement()
    {
        return new MNetPreset();
    }

    @Override
    protected boolean consumeElement(MNetPreset preset)
    {
        return new ClimateTrayPresetDialogController().consume(getView(), preset);
    }

    @Override
    public void modified(MessageList messages)
    {
        super.modified(messages);

        if (listModel.getSize() == 0)
        {
            messages.addWarning("Please, add at least one preset for managing a device.");
        }
    }

}
