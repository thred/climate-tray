package io.github.thred.climatetray.mnet.request;

import static io.github.thred.climatetray.ClimateTray.*;
import io.github.thred.climatetray.mnet.MNetDevice;
import io.github.thred.climatetray.util.DomBuilder;
import io.github.thred.climatetray.util.DomUtils;
import io.github.thred.climatetray.util.Utils;
import io.github.thred.climatetray.util.message.Message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public abstract class AbstractMNetRequest implements MNetRequest
{

    public AbstractMNetRequest()
    {
        super();
    }

    @Override
    public final void execute(URL url) throws MNetRequestException
    {
        try
        {
            String content = buildRequest();
            StringEntity body = new StringEntity(content);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(url.toURI());

            post.setHeader("content-type", "text/xml");
            post.setEntity(body);

            LOG.debug("Sending request to \"%s\". The request is:\n%s", url.toExternalForm(), content);

            CloseableHttpResponse response;

            try
            {
                response = client.execute(post);
            }
            catch (IOException e)
            {
                throw new MNetRequestException("Failed to send request to \"%s\".", e, url.toExternalForm())
                    .hint(Message
                        .error(
                            "Could not contact the centralized controller.\n\nThis usually indicates, that the value of the field \"Controller Address\" is wrong. "
                                + "If you are sure, that the value is correct, there may be a firewall or a proxy in the way. "
                                + "Try to call the URL \"%s\" in a browser.", url.toExternalForm()));
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
                                if (LOG.isDebugEnabled())
                                {
                                    byte[] bytes = Utils.readFully(in);

                                    LOG.debug("Reading response from \"%s\". The response is:\n%s",
                                        url.toExternalForm(), new String(bytes, "UTF-8"));

                                    in = new ByteArrayInputStream(bytes);
                                }

                                parseResponse(in);
                            }
                            finally
                            {
                                in.close();
                            }
                        }
                        catch (MNetRequestException e)
                        {
                            throw e;
                        }
                        catch (Exception e)
                        {
                            throw new MNetRequestException("Failed to parse response from \"%s\".", e,
                                url.toExternalForm())
                                .hint(Message
                                    .error(
                                        "The parsing of the response failed.\n\n"
                                            + "The request hit a server, but it may be the wrong one (the log may contain a more detailed description). "
                                            + "Check the contents of the field \"Controller Address\" or try to call the URL \"%s\" in a browser.",
                                        url.toExternalForm()));
                        }
                    }
                }
                else
                {
                    throw new MNetRequestException("Request to \"%s\" failed with error %d.", url.toExternalForm(),
                        status)
                        .hint(Message
                            .error(
                                "The request failed with error %d.\n\n"
                                    + "The request hit a server, but it may be the wrong one. "
                                    + "Check the contents of the field \"Controller Address\" again or try to call the URL \"%s\" in a browser.",
                                status, url.toExternalForm()));
                }
            }
            finally
            {
                response.close();
            }
        }
        catch (MNetRequestException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new MNetRequestException("Sending an request to \"%s\" failed with an unhandled error: %s.", e,
                url.toExternalForm(), e.toString())
                .hint(Message
                    .error("The request failed for some unknown reason.\n\nYou can check the log for the detailed exception."));
        }
    }

    protected final String buildRequest() throws MNetRequestException
    {
        DomBuilder builder = new DomBuilder();

        builder.begin("Packet");
        {
            builder.element("Command", getRequestCommand());
            builder.begin("DatabaseManager");
            {
                buildRequestContent(builder);
            }
            builder.end();
        }
        builder.end();

        return DomUtils.toString(builder.getDocument());
    }

    protected abstract String getRequestCommand();

    protected abstract void buildRequestContent(DomBuilder builder) throws MNetRequestException;

    public void parseResponse(InputStream inputStream) throws IOException, MNetRequestException
    {
        Document document = DomUtils.read(inputStream);
        Node errorNode = DomUtils.find(document, "//ERROR");

        if (errorNode != null)
        {
            String point = DomUtils.getAttribute(errorNode, "Point");
            String code = DomUtils.getAttribute(errorNode, "Code");
            String message = DomUtils.getAttribute(errorNode, "Message");

            MNetRequestException e;

            if (Utils.isBlank(point))
            {
                e = new MNetRequestException("The response contained the error \"%s\" (%s).", message, code);
            }
            else
            {
                e =
                    new MNetRequestException("The response contained the error \"%s\" (%s) at %s.", message, code,
                        point);
            }

            throw e
                .hint(Message
                    .error(
                        "The response contained the error \"%s\".\n\n"
                            + "This indicates that the centralized controller was successfully contacted, but it did not understand the request. "
                            + "Please make sure that the values of the fields \"EC\" and \"Air Conditioner Address\" are correct.",
                        message));
        }

        parseResponseContent(document);
    }

    protected abstract void parseResponseContent(Node document) throws MNetRequestException;

    public String describe(MNetDevice device)
    {
        try
        {
            return buildRequest();
        }
        catch (MNetRequestException e)
        {
            LOG.error("Failed to describe request.", e);

            return String.format("Failed to describe request: %s", e.toString());
        }
    }
}
