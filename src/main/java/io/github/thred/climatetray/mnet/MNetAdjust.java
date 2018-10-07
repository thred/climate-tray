package io.github.thred.climatetray.mnet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.thred.climatetray.ClimateTrayPreferences;
import io.github.thred.climatetray.util.Copyable;

public class MNetAdjust implements MNetDeviceList, MNetPresetList
{

    public static MNetAdjust of(ClimateTrayPreferences preferences)
    {
        return new MNetAdjust(Copyable.deepCopy(preferences.getDevices()), Copyable.deepCopy(preferences.getPresets()),
            new MNetPreset().set(preferences.getDefaultDevice()));
    }

    public static MNetAdjust of(ClimateTrayPreferences preferences, MNetDevice device)
    {
        return new MNetAdjust(new ArrayList<>(Arrays.asList(device.deepCopy())),
            Copyable.deepCopy(preferences.getPresets()), new MNetPreset().set(device));
    }

    private final List<MNetDevice> devices;
    private final List<MNetPreset> presets;
    private final MNetPreset preset;

    public MNetAdjust(List<MNetDevice> devices, List<MNetPreset> presets, MNetPreset preset)
    {
        super();
        this.devices = devices;
        this.presets = presets;
        this.preset = preset;
    }

    @Override
    public List<MNetDevice> getDevices()
    {
        return devices;
    }

    @Override
    public List<MNetPreset> getPresets()
    {
        return presets;
    }

    public MNetPreset getPreset()
    {
        return preset;
    }

}
