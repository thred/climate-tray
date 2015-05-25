/*
 * Copyright 2015 Manfred Hantschel
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
package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;

public abstract class AbstractMNetDeviceRequest extends AbstractMNetRequest implements Iterable<MNetDeviceRequestItem>
{

    protected final List<MNetDeviceRequestItem> requestItems = new ArrayList<MNetDeviceRequestItem>();
    protected final List<MNetDeviceRequestItem> responseItems = new ArrayList<MNetDeviceRequestItem>();

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
