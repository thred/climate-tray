package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

public abstract class AbstractMNetDeviceRequest extends AbstractMNetRequest implements Iterable<MNetDeviceRequestItem>
{

    private final List<MNetDeviceRequestItem> requestItems = new ArrayList<MNetDeviceRequestItem>();
    private final List<MNetDeviceRequestItem> responseItems = new ArrayList<MNetDeviceRequestItem>();

    public AbstractMNetDeviceRequest()
    {
        super();
    }

    protected void addRequestItem(MNetDeviceRequestItem item)
    {
        requestItems.add(item);
    }

    @Override
    public Iterator<MNetDeviceRequestItem> iterator()
    {
        return responseItems.iterator();
    }

    @Override
    protected void buildRequestContent(DomBuilder builder) throws MNetRequestException
    {
        for (MNetDeviceRequestItem item : requestItems)
        {
            builder.begin("Mnet");
            {
                buildRequestItemContent(builder, item);
            }
            builder.end();
        }
    }

    protected abstract void buildRequestItemContent(DomBuilder builder, MNetDeviceRequestItem item)
        throws MNetRequestException;

    @Override
    protected void parseResponseContent(Node document) throws MNetRequestException
    {
        List<Node> nodes = DomUtils.findAll(document, "//Mnet");

        for (Node node : nodes)
        {
            responseItems.add(parseResponseContentItem(node));
        }
    }

    protected MNetDeviceRequestItem parseResponseContentItem(Node node) throws MNetRequestException
    {
        return MNetDeviceRequestItem.parse(node);
    }

}
