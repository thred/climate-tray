package io.github.thred.climatetray.sim;

public class ClimateTrayServerSimState
{
    private final String address;
    private final String group;
    private final String model;

    private String drive = "OFF";
    private String mode = "COOL";
    private Double temperature = 22.5;
    private String air = "HORIZONTAL";
    private String fan = "LOW";

    public ClimateTrayServerSimState(String address, String group, String model)
    {
        super();
        this.address = address;
        this.group = group;
        this.model = model;
    }

    public String getAddress()
    {
        return address;
    }

    public String getGroup()
    {
        return group;
    }

    public String getModel()
    {
        return model;
    }

    public String getDrive()
    {
        return drive;
    }

    public void setDrive(String drive)
    {
        this.drive = drive;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }

    public Double getTemperature()
    {
        return temperature;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    public String getAir()
    {
        return air;
    }

    public void setAir(String air)
    {
        this.air = air;
    }

    public String getFan()
    {
        return fan;
    }

    public void setFan(String fan)
    {
        this.fan = fan;
    }

    @Override
    public String toString()
    {
        return "ClimateTrayServerSimState [address=" + address + ", group=" + group + ", model=" + model + ", drive="
            + drive + ", mode=" + mode + ", temperature=" + temperature + ", air=" + air + ", fan=" + fan + "]";
    }

}
