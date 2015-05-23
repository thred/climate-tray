package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.util.Message;

public class MNetRequestException extends Exception
{

    private static final long serialVersionUID = -3649539387632797521L;

    private Message hint;

    public MNetRequestException(String message, Object... args)
    {
        super(String.format(message, args));
    }

    public MNetRequestException(String message, Throwable cause, Object... args)
    {
        super(String.format(message, args), cause);
    }

    public Message getHint()
    {
        return hint;
    }

    public MNetRequestException hint(Message hint)
    {
        this.hint = hint;

        return this;
    }

    public Message toMessage()
    {
        return Message.error(getMessage(), getCause());
    }
}
