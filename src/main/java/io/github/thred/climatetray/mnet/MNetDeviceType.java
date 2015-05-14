package io.github.thred.climatetray.mnet;


public class MNetDeviceType
{

    public static final MNetDeviceType INSTANCE = new MNetDeviceType();

    private MNetDeviceType()
    {
        super();
    }

    public String getName()
    {
        return "Mitsubishi Electric";
    }

    public MNetDevice createDevice()
    {
        return new MNetDevice();
    }

}
