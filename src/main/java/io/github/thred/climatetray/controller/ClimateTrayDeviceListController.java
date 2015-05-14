package io.github.thred.climatetray.controller;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetDeviceCellRenderer;
import io.github.thred.climatetray.util.swing.AdvancedListModel;

import javax.swing.JList;

public class ClimateTrayDeviceListController extends AbstractClimateTrayListController<MNetDevice>
{

    public ClimateTrayDeviceListController()
    {
        super();
    }

    @Override
    protected JList<MNetDevice> createList(AdvancedListModel<MNetDevice> listModel)
    {
        JList<MNetDevice> list = super.createList(listModel);

        list.setCellRenderer(new MNetDeviceCellRenderer());

        return list;
    }

    @Override
    protected MNetDevice createInstance()
    {
        return new MNetDevice();
    }

    @Override
    protected boolean edit(MNetDevice device)
    {
        return new ClimateTrayDeviceDialogController().consume(getView(), device);
    }

}
