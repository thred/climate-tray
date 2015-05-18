package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;

import org.w3c.dom.Node;

public class MNetInfoRequest extends MNetRequest
{

    private Integer group;
    private String model;

    public MNetInfoRequest()
    {
        super();
    }

    public Integer getGroup()
    {
        return group;
    }

    public String getModel()
    {
        return model;
    }

    @Override
    protected String getRequestCommand()
    {
        return "getRequest";
    }

    @Override
    protected void buildRequestContent(DomBuilder builder, MNetDevice device)
    {
        builder.begin("Mnet");
        builder.attribute("Address", device.getAddress());
        builder.attribute("Group", "*");
        builder.attribute("Model", "*");
        builder.end();
    }

    @Override
    protected void parseResponseContent(Node node)
    {
        group = DomUtils.getIntegerAttribute(node, "Group", null);
        model = DomUtils.getAttribute(node, "Model", null);
    }
}
