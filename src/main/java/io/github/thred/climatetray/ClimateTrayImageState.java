package io.github.thred.climatetray;

public enum ClimateTrayImageState
{

    NONE(""),
    SELECTED(":selected"),
    NOT_SELECTED(":not-selected");

    private final String postfix;

    private ClimateTrayImageState(String postfix)
    {
        this.postfix = postfix;
    }

    public String getPostfix()
    {
        return postfix;
    }

}
