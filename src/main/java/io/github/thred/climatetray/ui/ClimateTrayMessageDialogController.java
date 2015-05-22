package io.github.thred.climatetray.ui;

import io.github.thred.climatetray.util.Message;

import java.awt.Window;

public class ClimateTrayMessageDialogController extends DefaultClimateTrayDialogController<Message>
{

    public static Button consumeOkDialog(Window owner, String title, Message message)
    {
        ClimateTrayMessageDialogController controller = new ClimateTrayMessageDialogController(owner, Button.OK);

        controller.setTitle(title);

        return controller.consume(message);
    }

    public ClimateTrayMessageDialogController(Window owner, Button... buttons)
    {
        super(owner, new ClimateTrayMessageController(), buttons);
    }

}
