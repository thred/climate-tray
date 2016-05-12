/*
 * Copyright 2015, 2016 Manfred Hantschel
 *
 * This file is part of Climate-Tray.
 *
 * Climate-Tray is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * Climate-Tray is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Climate-Tray. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package io.github.thred.climatetray.sim;

import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Controller
@RequestMapping(value = "/servlet/MIMEReceiveServlet", consumes = "text/xml", produces = "text/xml")
public class ClimateTrayServerSimController
{

    private static final long LAG = 250;
    private final List<ClimateTrayServerSimState> states = new ArrayList<>();

    public ClimateTrayServerSimController()
    {
        super();

        states.add(new ClimateTrayServerSimState("17", "22", "IC"));
        states.add(new ClimateTrayServerSimState("18", "24", "IC"));
        states.add(new ClimateTrayServerSimState("19", "26", "IC"));
    }

    protected ClimateTrayServerSimState getStateByAddress(String address)
    {
        return states.stream().filter(state -> Objects.equals(address, state.getAddress())).findFirst().orElse(null);
    }

    protected ClimateTrayServerSimState getStateByGroup(String group)
    {
        return states.stream().filter(state -> Objects.equals(group, state.getGroup())).findFirst().orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String mNet(@RequestBody String body)
    {
        Document requestDoc = DomUtils.read(body);
        DomBuilder responseBuilder = new DomBuilder();

        responseBuilder.begin("Packet");

        String command = DomUtils.getText(DomUtils.find(requestDoc, "//Command"), null);

        switch (command)
        {
            case "getRequest":
                getRequest(requestDoc, responseBuilder);
                break;

            case "setRequest":
                setRequest(requestDoc, responseBuilder);
                break;

            default:
                throw new IllegalArgumentException("Unsupported request: " + body);
        }

        responseBuilder.end();

        try
        {
            Thread.sleep(LAG);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return responseBuilder.toString();
    }

    private void getRequest(Document requestDoc, DomBuilder responseBuilder)
    {
        responseBuilder.element("Command", "getResponse");
        responseBuilder.begin("DatabaseManager");

        DomUtils.findAll(requestDoc, "//Mnet").forEach(requestNode -> getRequestElement(requestNode, responseBuilder));

        responseBuilder.end();
    }

    private void getRequestElement(Node requestNode, DomBuilder responseBuilder)
    {
        String ec = DomUtils.getAttribute(requestNode, "Ec");
        String address = DomUtils.getAttribute(requestNode, "Address");
        String group = DomUtils.getAttribute(requestNode, "Group");

        if ("0".equals(ec))
        {
            // invalid ec
            if (address != null)
            {
                // info request
                responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address)
                    .attribute("Group", group).end();
            }
            else
            {
                throw new UnsupportedOperationException();
            }
        }
        else if (!"1".equals(ec))
        {
            // wrong ec
            if (address != null)
            {
                // info request
                unknwonDevice(responseBuilder, ec, address);
            }
            else
            {
                throw new UnsupportedOperationException();
            }
        }
        else
        {
            ClimateTrayServerSimState state = getStateByAddress(address);

            if (state != null)
            {
                // info request
                responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address)
                    .attribute("Group", state.getGroup()).attribute("Model", state.getModel()).end();
            }
            else if (address != null)
            {
                // info request
                unknwonDevice(responseBuilder, ec, address);
            }

            state = getStateByGroup(group);

            if (state != null)
            {
                System.err.println("Returning state: " + state);

                appendState(
                    responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address)
                        .attribute("Model", state.getModel()), state).end();
            }
            else
            {
                // info request
                unknwonDevice(responseBuilder, ec, address);
            }
        }

        if ("0".equals(ec))
        {
            responseBuilder.begin("ERROR").attribute("Point", "Ec[0]").attribute("Code", "0201")
                .attribute("Message", "Invalid Value").end();
        }
    }

    private void setRequest(Document requestDoc, DomBuilder responseBuilder)
    {
        responseBuilder.element("Command", "setResponse");
        responseBuilder.begin("DatabaseManager");

        DomUtils.findAll(requestDoc, "//Mnet").forEach(requestNode -> setRequestElement(requestNode, responseBuilder));

        responseBuilder.end();
    }

    private void setRequestElement(Node requestNode, DomBuilder responseBuilder)
    {
        String ec = DomUtils.getAttribute(requestNode, "Ec");

        if (!"1".equals(ec))
        {
            // unknown EC
            return;
        }

        String group = DomUtils.getAttribute(requestNode, "Group");
        ClimateTrayServerSimState state = getStateByGroup(group);

        if (state == null)
        {
            // unknown group
            return;
        }

        state.setDrive(DomUtils.getAttribute(requestNode, "Drive", state.getDrive()));
        state.setMode(DomUtils.getAttribute(requestNode, "Mode", state.getMode()));
        state.setTemperature(DomUtils.getDoubleAttribute(requestNode, "SetTemp", state.getTemperature()));
        state.setAir(DomUtils.getAttribute(requestNode, "AirDirection", state.getAir()));
        state.setFan(DomUtils.getAttribute(requestNode, "FanSpeed", state.getFan()));

        System.err.println("State set to: " + state);

        appendState(responseBuilder.begin("Mnet").attribute("Ec", ec), state).end();
    }

    private DomBuilder appendState(DomBuilder responseBuilder, ClimateTrayServerSimState state)
    {
        double thermometer = (Math.random() * 20) + 17;

        responseBuilder.attribute("Drive", state.getDrive()).attribute("Group", state.getGroup());

        if ("AUTO".equals(state.getMode()))
        {
            if ((thermometer + 3) < state.getTemperature().doubleValue())
            {
                responseBuilder.attribute("Mode", "AUTOHEAT");
            }
            else if ((thermometer - 3) > state.getTemperature().doubleValue())
            {
                responseBuilder.attribute("Mode", "AUTOCOOL");
            }
            else
            {
                responseBuilder.attribute("Mode", "AUTO");
            }
        }
        else
        {
            responseBuilder.attribute("Mode", state.getMode());
        }

        responseBuilder.attribute("SetTemp", String.format(Locale.ENGLISH, "%.1f", state.getTemperature()))
            .attribute("InletTemp", String.format(Locale.ENGLISH, "%.1f", thermometer))
            .attribute("AirDirection", state.getAir()).attribute("FanSpeed", state.getFan());

        return responseBuilder;
    }

    public DomBuilder unknwonDevice(DomBuilder responseBuilder, String ec, String address)
    {
        return responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address).attribute("Group", "99")
            .attribute("Model", "NONE").end();
    }

}