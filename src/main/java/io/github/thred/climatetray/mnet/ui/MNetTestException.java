package io.github.thred.climatetray.mnet.ui;


public class MNetTestException extends RuntimeException
{

    private static final long serialVersionUID = -8978733427880485431L;

    private final Object[] args;

    public MNetTestException()
    {
        super();

        args = new Object[0];
    }

    public MNetTestException(String message, Object... args)
    {
        super(message);

        this.args = args;
    }

    public MNetTestException(String message, Throwable cause, Object... args)
    {
        super(message, cause);

        this.args = args;
    }

    public Object[] getArgs()
    {
        return args;
    }

}
