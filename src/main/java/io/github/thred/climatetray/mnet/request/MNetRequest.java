package io.github.thred.climatetray.mnet.request;

import io.github.thred.climatetray.ClimateTray;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.mnet.MNetStateType;
import io.github.thred.climatetray.mnet.MNetUtils;
import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;
import io.github.thred.climatetray.util.MessageBuffer;
import io.github.thred.climatetray.util.Severity;
import io.github.thred.climatetray.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class MNetRequest
{

    public MNetRequest()
    {
        super();
    }

    public final boolean execute(MNetDevice device, MessageBuffer messages)
    {
        if (messages == null)
        {
            messages = new MessageBuffer();
        }

        try
        {
            String content = buildRequest(device);
            StringEntity body = new StringEntity(content);
            CloseableHttpClient client = HttpClients.createDefault();
            URL url = MNetUtils.toURL(device.getHost());
            HttpPost post = new HttpPost(url.toURI());

            post.setHeader("content-type", "text/xml");
            post.setEntity(body);

            ClimateTray.LOG.debug("Sending request to %s:\n%s", device.describe(true, MNetStateType.NONE), content);

            CloseableHttpResponse response;

            try
            {
                response = client.execute(post);
            }
            catch (IOException e)
            {
                messages
                    .error(
                        "Could not contact the device.\n\nThis usually indicates, that the host / URL is wrong. "
                            + "If you are sure, that the host / URL is correct, there may be a firewall or a proxy in the way. "
                            + "Try to call the URL \"%s\" in a browser.", url.toExternalForm());

                ClimateTray.LOG.error("Failed to send request to %s.", e, device.describe(true, MNetStateType.NONE));

                return false;
            }

            try
            {
                int status = response.getStatusLine().getStatusCode();

                if ((status >= 200) && (status < 300))
                {
                    HttpEntity entity = response.getEntity();

                    if (entity != null)
                    {
                        try
                        {
                            InputStream in = entity.getContent();

                            try
                            {
                                if (ClimateTray.LOG.isDebugEnabled())
                                {
                                    byte[] bytes = Utils.readFully(in);

                                    ClimateTray.LOG.debug("Reading response from %s:\n%s",
                                        device.describe(true, MNetStateType.NONE), new String(bytes, "UTF-8"));

                                    in = new ByteArrayInputStream(bytes);
                                }

                                parseResponse(messages, in);
                            }
                            finally
                            {
                                in.close();
                            }
                        }
                        catch (Exception e)
                        {
                            messages.error("The parsing of the response failed.\n\n"
                                + "The request hit a server, but it may be the wrong one. "
                                + "You can check the log for the detailed exception.");

                            ClimateTray.LOG.error("Failed to parse response.", e);

                            return false;
                        }

                        if (messages.containsAtLeast(Severity.ERROR))
                        {
                            return false;
                        }
                    }
                }
                else
                {
                    messages.error("The request failed with error %d.\n\n"
                        + "The request hit a server, but it may be the wrong one. "
                        + "Check the host / URL again or try to call the URL \"%s\" in a browser.", status,
                        url.toExternalForm());

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
            messages
                .error("The request failed for some unknown reason.\n\nYou can check the log for the detailed exception.");

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

    public void parseResponse(MessageBuffer messages, InputStream inputStream) throws IOException
    {
        Document document = DomUtils.read(inputStream);
        List<Node> errors = DomUtils.findAll(document, "//ERROR");

        if (errors.size() > 0)
        {
            errors.forEach((node) -> {
                String point = DomUtils.getAttribute(node, "Point");
                String code = DomUtils.getAttribute(node, "Code");
                String message = DomUtils.getAttribute(node, "Message");

                if (Utils.isBlank(point))
                {
                    messages.error("Device returned error %s (%s).", message, code);
                }
                else
                {
                    messages.error("Device returned error %s (%s) at %s.", message, code, point);
                }
            });
        }

        parseResponseContent(document);
    }

    protected abstract void parseResponseContent(Node document);

    public String describe(MNetDevice device)
    {
        return buildRequest(device);
    }
}
