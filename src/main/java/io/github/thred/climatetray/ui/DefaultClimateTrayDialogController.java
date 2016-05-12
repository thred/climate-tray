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
package io.github.thred.climatetray.ui;

import java.awt.Dialog.ModalityType;
import java.awt.Window;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.WindowConstants;

public class DefaultClimateTrayDialogController<MODEL_TYPE> extends
    AbstractClimateTrayWindowController<MODEL_TYPE, JDialog>
{

    public DefaultClimateTrayDialogController(Window owner,
        AbstractClimateTrayController<MODEL_TYPE, ? extends JComponent> controller, Button... buttons)
    {
        super(owner, controller, buttons);
    }

    @Override
    protected JDialog createWindow()
    {
        JDialog window = new JDialog(owner, ModalityType.DOCUMENT_MODAL);

        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        return window;
    }

    @Override
    public void setTitle(String title)
    {
        getView().setTitle(title);

        super.setTitle(title);
    }

    @Override
    public void setTitle(Icon icon, String title)
    {
        getView().setTitle(title);

        super.setTitle(icon, title);
    }

}
