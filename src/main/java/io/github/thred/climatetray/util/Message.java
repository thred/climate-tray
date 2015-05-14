package io.github.thred.climatetray.util;

public class Message
{

    private final Severity severity;
    private final String message;

    public Message(Severity severity, String message, Object... args)
    {
        super();

        this.severity = severity;
        this.message = String.format(message, args);
    }

    public Severity getSeverity()
    {
        return severity;
    }

    public String getMessage()
    {
        return message;
    }

    @Override
    public String toString()
    {
        return String.format("%s: %s", severity, message);
    }
}
