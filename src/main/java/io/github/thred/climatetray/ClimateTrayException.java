package io.github.thred.climatetray;

public class ClimateTrayException extends RuntimeException
{

    private static final long serialVersionUID = -3231901958431158724L;

    public ClimateTrayException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ClimateTrayException(String message)
    {
        super(message);
    }

}
