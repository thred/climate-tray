package io.github.thred.climatetray.mnet.request;

import java.net.URL;

public interface MNetRequest
{

    void execute(URL url) throws MNetRequestException;

}
