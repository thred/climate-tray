package io.github.thred.climatetray.mnet;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.ClimateTrayService;
import io.github.thred.climatetray.mnet.request.MNetDeviceRequestItem;
import io.github.thred.climatetray.mnet.request.MNetInfoRequest;
import io.github.thred.climatetray.mnet.request.MNetMonitorRequest;
import io.github.thred.climatetray.mnet.request.MNetRequestException;

import java.net.MalformedURLException;

public class MNetDeviceService
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

    public static void update(MNetDevice device)
    {
        if (!device.isEnabled())
        {
            return;
        }

        if (!ensureGroup(device))
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
                item.update(device);
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

    public static boolean ensureGroup(MNetDevice device)
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
                item.update(device);
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

}
