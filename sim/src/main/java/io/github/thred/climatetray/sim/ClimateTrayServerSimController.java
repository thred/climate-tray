package io.github.thred.climatetray.sim;

import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;

import java.util.Locale;

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

    private static final String GROUP = "22";
    private static final String MODEL = "IC";

    private String drive = "OFF";
    private String mode = "COOL";
    private Double temperature = 22.5;
    private String air = "HORIZONTAL";
    private String fan = "LOW";

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
            Thread.sleep(1000);
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
            if ("17".equals(address))
            {
                // info request
                responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address)
                    .attribute("Group", GROUP).attribute("Model", MODEL).end();
            }
            else if (address != null)
            {
                // info request
                unknwonDevice(responseBuilder, ec, address);
            }
            else if ("22".equals(group))
            {
                System.err.println("Returning state: " + this);

                appendState(responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address).attribute("Model", MODEL)).end();
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

        if (!"22".equals(group))
        {
            // unknown group
            return;
        }

        drive = DomUtils.getAttribute(requestNode, "Drive", drive);
        mode = DomUtils.getAttribute(requestNode, "Mode", mode);
        temperature = DomUtils.getDoubleAttribute(requestNode, "temperature", temperature);
        air = DomUtils.getAttribute(requestNode, "AirDirection", air);
        fan = DomUtils.getAttribute(requestNode, "FanSpeed", fan);

        System.err.println("State set to: " + this);

        appendState(responseBuilder.begin("Mnet").attribute("Ec", ec)).end();
    }

    private DomBuilder appendState(DomBuilder responseBuilder)
    {
        double thermometer = (Math.random() * 20) + 17;

        responseBuilder.attribute("Drive", drive).attribute("Group", GROUP);

        if ("AUTO".equals(mode))
        {
            if ((thermometer + 3) < temperature.doubleValue())
            {
                responseBuilder.attribute("Mode", "AUTOHEAT");
            }
            else if ((thermometer - 3) > temperature.doubleValue())
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
            responseBuilder.attribute("Mode", mode);
        }

        responseBuilder.attribute("SetTemp", String.format(Locale.ENGLISH, "%.1f", temperature))
            .attribute("InletTemp", String.format(Locale.ENGLISH, "%.1f", thermometer)).attribute("AirDirection", air)
            .attribute("FanSpeed", fan);

        return responseBuilder;
    }

    public DomBuilder unknwonDevice(DomBuilder responseBuilder, String ec, String address)
    {
        return responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address).attribute("Group", "99")
            .attribute("Model", "NONE").end();
    }

    @Override
    public String toString()
    {
        return "ClimateTrayServerSimController [drive=" + drive + ", mode=" + mode + ", temperature=" + temperature
            + ", air=" + air + ", fan=" + fan + "]";
    }

}