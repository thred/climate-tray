package io.github.thred.climatetray.mnet;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;

public abstract class MNetRequest
{

    public MNetRequest()
    {
        super();
    }

    public final boolean execute(MNetDevice device)
    {
        try
        {
            String content = buildRequest(device);
            StringEntity body = new StringEntity(content);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(MNetUtils.toURL(device.getHost()).toURI());

            post.setHeader("content-type", "text/xml");
            post.setEntity(body);

            ClimateTray.LOG.debug("Sending request to %s: %s", device.describe(true, MNetStateType.NONE), content);

            CloseableHttpResponse response = client.execute(post);

            try
            {
                int status = response.getStatusLine().getStatusCode();

                if ((status >= 200) && (status < 300))
                {
                    HttpEntity entity = response.getEntity();

                    if (entity != null)
                    {
                        parseResponse(entity.getContent());
                    }
                }
                else
                {
                    ClimateTray.LOG.error("Request to %s failed with error %d: %s",
                        device.describe(true, MNetStateType.NONE), status,
                        EntityUtils.toString(response.getEntity(), "UTF-8"));

                    return false;
                }
            }
            finally
            {
                response.close();
            }

            return true;
        }
        catch (Exception e)
        {
            ClimateTray.LOG.error("Failed to send request to %s.", e, device.describe(true, MNetStateType.NONE));

            return false;
        }
    }

    protected final String buildRequest(MNetDevice device)
    {
        DomBuilder builder = new DomBuilder();

        builder.begin("Packet");
        {
            builder.element("Command", getRequestCommand());
            builder.begin("DatabaseManager");
            {
                buildRequestContent(builder, device);
            }
            builder.end();
        }
        builder.end();

        return DomUtils.toString(builder.getDocument());
    }

    protected abstract String getRequestCommand();

    protected abstract void buildRequestContent(DomBuilder builder, MNetDevice device);

    public void parseResponse(InputStream inputStream) throws IOException
    {
        DomUtils.findAll(DomUtils.read(inputStream), "//Mnet").forEach((node) -> parseResponseContent(node));
    }

    protected abstract void parseResponseContent(Node node);

    public String describe(MNetDevice device)
    {
        return buildRequest(device);
    }
}
