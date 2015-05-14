package io.github.thred.climatetray;

import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetPreset;
import io.github.thred.climatetray.util.Persistent;
import io.github.thred.climatetray.util.prefs.Prefs;

import java.util.ArrayList;
import java.util.List;

public class ClimateTrayPreferences implements Persistent
{

    private final List<MNetDevice> devices = new ArrayList<MNetDevice>();
    private final List<MNetPreset> presets = new ArrayList<MNetPreset>();

    public ClimateTrayPreferences()
    {
        super();
    }

    public List<MNetDevice> getDevices()
    {
        return devices;
    }

    public List<MNetPreset> getPresets()
    {
        return presets;
    }

    @Override
    public void read(Prefs prefs)
    {
        Persistent.readList(prefs, "device", devices, () -> new MNetDevice());
        Persistent.readList(prefs, "preset", presets, () -> new MNetPreset());
    }

    @Override
    public void write(Prefs prefs)
    {
        Persistent.writeList(prefs, "device", devices);
        Persistent.writeList(prefs, "preset", presets);
    }
}
