package io.github.thred.climatetray.mnet.ui;

import io.github.thred.climatetray.util.Message;
import io.github.thred.climatetray.util.Severity;

public class MNetTestException extends Exception
{

    private static final long serialVersionUID = -8978733427880485431L;

    private final Severity severity;

    public MNetTestException(Severity severity, String message, Object... args)
    {
        super(String.format(message, args));

        this.severity = severity;
    }

    public MNetTestException(Severity severity, String message, Throwable cause, Object... args)
    {
        super(String.format(message, args), cause);

        this.severity = severity;
    }

    public Message toMessage()
    {
        return new Message(severity, getMessage(), getCause());
    }

}
