package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.util.MessageBuffer;

public class MNetRequestException extends Exception
{

    private static final long serialVersionUID = -3649539387632797521L;
    
    private final MessageBuffer hints = new MessageBuffer();

}
