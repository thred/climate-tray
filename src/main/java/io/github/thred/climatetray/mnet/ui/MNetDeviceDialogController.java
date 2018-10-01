/*
 * Copyright 2015 - 2018 Manfred Hantschel
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
package io.github.thred.climatetray.mnet.ui;

import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.ui.MNetTest.State;
import io.github.thred.climatetray.ui.DefaultClimateTrayDialogController;
import io.github.thred.climatetray.util.Severity;
import io.github.thred.climatetray.util.message.Message;
import io.github.thred.climatetray.util.message.MessageBuffer;
import io.github.thred.climatetray.util.swing.FooterPanel;
import io.github.thred.climatetray.util.swing.SwingUtils;

public class MNetDeviceDialogController extends DefaultClimateTrayDialogController<MNetDevice>
{

    protected JButton testButton;

    public MNetDeviceDialogController(Window owner)
    {
        super(owner, new MNetDeviceController(), Button.OK, Button.CANCEL);

        setTitle("Air Conditioner Settings");
        setDescription(Message.info("Look on the air conditioner for the necessary informations."));

        okButton.setEnabled(false);
        testButton.setEnabled(false);
    }

    @Override
    public void modified(MessageBuffer messageBuffer)
    {
        super.modified(messageBuffer);

        testButton.setEnabled(!messageBuffer.containsAtLeast(Severity.ERROR));
    }

    @Override
    protected JComponent createBottomPanel(Button... buttons)
    {
        FooterPanel panel = (FooterPanel) super.createBottomPanel(buttons);

        panel.left(testButton = SwingUtils.createButton("Test", (e) -> test()));

        return panel;
    }

    public void test()
    {
        SwingUtilities.invokeLater(() -> {
            MNetDevice device = new MNetDevice();

            applyTo(device);

            device.setEnabled(true);
            device.setSelected(true);

            MNetTest test = new MNetTest(device);

            new MNetTestDialogController(getView()).consume(test);

            if (test.getState() == State.SUCCEEDED)
            {
                prepareWith(device);
            }
        });
    }
}
