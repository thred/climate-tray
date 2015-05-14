package io.github.thred.climatetray.util;

import java.util.ArrayList;
import java.util.Collection;

public class MessageList extends ArrayList<Message>
{

    private static final long serialVersionUID = 2788447149905230681L;

    public MessageList()
    {
        super();
    }

    public MessageList(Collection<? extends Message> c)
    {
        super(c);
    }

    public void addInfo(String message, Object... args)
    {
        add(Severity.INFO, message, args);
    }

    public void addWarning(String message, Object... args)
    {
        add(Severity.WARNING, message, args);
    }

    public void addError(String message, Object... args)
    {
        add(Severity.ERROR, message, args);
    }

    public void add(Severity severity, String message, Object... args)
    {
        add(new Message(severity, message, args));
    }

    public boolean containsAtLeast(Severity severity)
    {
        for (Message message : this)
        {
            if (message.getSeverity().ordinal() >= severity.ordinal())
            {
                return true;
            }
        }

        return false;
    }

    public void sortBySeverity()
    {
        sort((left, right) -> (right.getSeverity().ordinal() - left.getSeverity().ordinal()));
    }
}
