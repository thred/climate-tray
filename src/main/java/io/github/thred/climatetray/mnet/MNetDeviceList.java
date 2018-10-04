package io.github.thred.climatetray.mnet;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public interface MNetDeviceList
{

    List<MNetDevice> getDevices();

    default MNetDevice getDevice(UUID id)
    {
        return getDevices().stream().filter((device) -> Objects.equals(id, device.getId())).findFirst().orElse(null);
    }

    default MNetDevice getDefaultDevice()
    {
        return getDevices().stream().filter(device -> device.isSelectedAndWorking()).findFirst().orElse(null);
    }

    default boolean isAnyDeviceSelected()
    {
        return getDevices().stream().filter(device -> device.isEnabled() && device.isSelected()).count() > 0;
    }

    default int countEnabledDevices()
    {
        return (int) getDevices().stream().filter(device -> device.isEnabled()).count();
    }

    default boolean isAnyDeviceSelectedAndWorking()
    {
        return getDevices().stream().filter(device -> device.isEnabled() && device.isSelectedAndWorking()).count() > 0;
    }

    default boolean isAnyDeviceEnabledAndWorking()
    {
        return getDevices().stream().filter(device -> device.isEnabled() && device.isWorking()).count() > 0;
    }

    default void selectFirstDevice()
    {
        getDevices()
            .stream()
            .filter(device -> device.isEnabled())
            .findFirst()
            .ifPresent(device -> device.setSelected(true));
    }

}
