package io.github.thred.climatetray.util;

@FunctionalInterface
public interface ExceptionConsumer
{

    void failed(Exception exception);

}
