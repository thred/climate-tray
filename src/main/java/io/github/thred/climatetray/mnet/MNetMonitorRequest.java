package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.util.DomBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

public class MNetMonitorRequest extends MNetRequest implements Iterable<MNetRequestElement>
{

    private final List<MNetRequestElement> requestElements = new ArrayList<MNetRequestElement>();
    private final List<MNetRequestElement> responseElements = new ArrayList<MNetRequestElement>();

    public MNetMonitorRequest()
    {
        super();
    }

    public void addRequestElement(MNetRequestElement requestElement)
    {
        requestElements.add(requestElement);
    }

    @Override
    public Iterator<MNetRequestElement> iterator()
    {
        return responseElements.iterator();
    }

    @Override
    protected String getRequestCommand()
    {
        return "setRequest";
    }

    @Override
    protected void buildRequestContent(DomBuilder builder, MNetDevice device)
    {
        requestElements.stream().forEach((element) -> element.buildMonitorRequest(builder));
    }

    @Override
    protected void parseResponseContent(Node node)
    {
        responseElements.add(MNetRequestElement.parse(node));
    }

}
