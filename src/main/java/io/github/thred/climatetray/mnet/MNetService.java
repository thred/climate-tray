package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.request.MNetDeviceRequestItem;
import io.github.thred.climatetray.mnet.request.MNetInfoRequest;
import io.github.thred.climatetray.mnet.request.MNetMonitorRequest;
import io.github.thred.climatetray.mnet.request.MNetOperateRequest;
import io.github.thred.climatetray.mnet.request.MNetRequestException;

import java.net.MalformedURLException;
import java.util.List;

public class MNetService
{

    public static void disable(MNetDevice device)
    {
        if (!device.isEnabled())
        {
            return;
        }

        LOG.info("Disabling air conditioner \"%s\".", device.getName());

        device.setEnabled(false);
        device.setSelected(false);

        ClimateTrayService.store();
    }

    public static void updateDevice(MNetDevice device)
    {
        if (!device.isEnabled())
        {
            return;
        }

        if (!ensureDeviceGroup(device))
        {
            LOG.error("Cannot update state of air conditioner \"%s\" without group value.", device.getName());

            disable(device);
        }

        MNetMonitorRequest request = new MNetMonitorRequest();

        try
        {
            request.addDevice(device).execute(device.getURL());

            MNetDeviceRequestItem item = request.getItemByDeviceGroup(device);

            if (item == null)
            {
                LOG.error("The centralized controller did not return a state for the air conditioner \"%s\".");

                disable(device);
            }
            else
            {
                updateDeviceState(device, item);
            }
        }
        catch (MalformedURLException e)
        {
            LOG.error("Invalid url for air conditioner \"%s\".", e, device.getName());

            disable(device);
        }
        catch (MNetRequestException e)
        {
            LOG.error("Failed to request info of air conditioner \"%s\".", e, device.getName());

            disable(device);
        }
    }

    public static void adjustDevice(MNetDevice device, MNetPreset preset)
    {
        MNetOperateRequest request = new MNetOperateRequest();

        try
        {
            request.adjustDevice(device, preset).execute(device.getURL());

            MNetDeviceRequestItem item = request.getItemByDeviceGroup(device);

            if (item == null)
            {
                LOG.error("The centralized controller did not return a state for the air conditioner \"%s\".");

                disable(device);
            }
            else
            {
                updateDeviceState(device, item);
            }
        }
        catch (MalformedURLException e)
        {
            LOG.error("Invalid url for air conditioner \"%s\".", e, device.getName());

            disable(device);
        }
        catch (MNetRequestException e)
        {
            LOG.error("Failed to request info of air conditioner \"%s\".", e, device.getName());

            disable(device);
        }
    }

    public static boolean ensureDeviceGroup(MNetDevice device)
    {
        if (device.getGroup() != null)
        {
            return true;
        }

        MNetInfoRequest request = new MNetInfoRequest();

        try
        {
            request.addDevice(device).execute(device.getURL());

            MNetDeviceRequestItem item = request.getItemByDeviceAddress(device);

            if (item != null)
            {
                updateDeviceState(device, item);
            }
        }
        catch (MalformedURLException e)
        {
            LOG.error("Invalid url for air conditioner %s.", e, device.getName());
        }
        catch (MNetRequestException e)
        {
            LOG.error("Failed to request info of air conditioner %s.", e, device.getName());
        }

        return device.getGroup() != null;
    }

    public static void updateDeviceState(MNetDevice device, MNetDeviceRequestItem item)
    {
        item.update(device);
    }

    public static boolean isMatching(MNetPreset preset, List<MNetState> states)
    {
        return states.stream().allMatch(state -> isMatching(preset, state));
    }

    public static boolean isMatching(MNetPreset preset, MNetState state)
    {
        MNetDrive drive = preset.getDrive();

        if (state.getDrive() == MNetDrive.OFF)
        {
            return drive == MNetDrive.OFF;
        }

        if ((drive != MNetDrive.NO_CHANGE) && (drive != state.getDrive()))
        {
            return false;
        }

        MNetMode mode = preset.getMode();

        if (mode != MNetMode.NO_CHANGE)
        {
            if ((mode == MNetMode.AUTO)
                && (!((state.getMode() == MNetMode.AUTO) || (state.getMode() == MNetMode.AUTO_HEAT) || (state.getMode() == MNetMode.AUTO_COOL))))
            {
                return false;
            }

            if (mode != state.getMode())
            {
                return false;
            }
        }

        Double temperature = preset.getTemperature();

        if ((temperature != null) && (Math.abs(temperature - state.getTemperature()) > 0.667))
        {
            return false;
        }

        MNetFan fan = preset.getFan();

        if ((fan != MNetFan.NO_CHANGE) && (fan != state.getFan()))
        {
            return false;
        }

        MNetAir air = preset.getAir();

        if ((air != MNetAir.NO_CHANGE) && (air != state.getAir()))
        {
            return false;
        }

        return true;
    }
}
