package io.github.thred.climatetray.mnet;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface MNetPresetList
{

    List<MNetPreset> getPresets();

    default boolean addPreset(MNetPreset preset)
    {
        MNetPreset existing = findSame(preset);

        if (existing != null)
        {
            return false;
        }

        getPresets().add(preset);

        return true;
    }

    default MNetPreset getPreset(UUID id)
    {
        return getPresets().stream().filter(preset -> Objects.equals(id, preset.getId())).findFirst().orElse(null);
    }

    default MNetPreset findSame(MNetPreset preset)
    {
        return getPresets().stream().filter(existing -> preset.isSame(existing)).findFirst().orElse(null);
    }

    default boolean isAnyPresetAvailable()
    {
        return !getPresets().isEmpty();
    }

}
