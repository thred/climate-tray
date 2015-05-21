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

    private final String drive = "OFF";
    private final String mode = "COOL";
    private final double temperature = 22.5;
    private final String air = "HORIZONTAL";
    private final String fan = "LOW";

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

            default:
                throw new IllegalArgumentException("Unsupported request: " + body);
        }

        responseBuilder.end();

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
                responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address)
                    .attribute("Group", GROUP).attribute("Model", MODEL).attribute("Drive", drive)
                    .attribute("Mode", mode).attribute("SetTemp", String.format(Locale.ENGLISH, "%.1f", temperature))
                    .attribute("InletTemp", String.format(Locale.ENGLISH, "%.1f", (Math.random() * 20) + 17))
                    .attribute("AirDirection", air).attribute("FanSpeed", fan).end();
            }
        }

        if ("0".equals(ec))
        {
            responseBuilder.begin("ERROR").attribute("Point", "Ec[0]").attribute("Code", "0201")
                .attribute("Message", "Invalid Value").end();
        }
    }

    public DomBuilder unknwonDevice(DomBuilder responseBuilder, String ec, String address)
    {
        return responseBuilder.begin("Mnet").attribute("Ec", ec).attribute("Address", address).attribute("Group", "99")
            .attribute("Model", "NONE").end();
    }
}