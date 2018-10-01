/*
 * Copyright 2015 - 2018 Manfred Hantschel
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
package io.github.thred.climatetray.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Some useful methods for XML parsing
 *
 * @author Manfred Hantschel
 */
public final class DomUtils
{

    /**
     * IO exception for DOM actions
     *
     * @author ham
     */
    public static class DOMIOException extends IOException
    {
        private static final long serialVersionUID = 4986111859321994696L;

        /**
         * Creates a new exception
         *
         * @param message the message
         * @param cause the cause
         */
        public DOMIOException(String message, Throwable cause)
        {
            super(message);

            initCause(cause);
        }
    }

    private DomUtils()
    {
        super();
    }

    /**
     * Creates a Document for the specified xml string
     *
     * @param xml the xml string
     * @return the document or null
     * @throws IllegalArgumentException if the XML could not be parsed
     */
    public static Document read(String xml) throws IllegalArgumentException
    {
        try
        {
            Reader reader = new StringReader(xml);

            try
            {
                return read(reader);
            }
            finally
            {
                reader.close();
            }
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Wierd! Cannot read from string reader", e);
        }
    }

    /**
     * Creates a Document for the specified resource
     *
     * @param classLoader the classLoader that contains the resource
     * @param resource the resource
     * @return the document or null
     * @throws IllegalArgumentException if the XML could not be parsed or read
     */
    public static Document read(ClassLoader classLoader, String resource) throws IllegalArgumentException
    {
        URL url = classLoader.getResource(resource);

        if (url != null)
        {
            return read(url);
        }

        throw new IllegalArgumentException("Could not find resource \"" + resource + "\"");
    }

    /**
     * Creates a Document for the specified URL
     *
     * @param url the URL
     * @return the document or null
     * @throws IllegalArgumentException if the XML could not be parsed or read
     */
    public static Document read(URL url) throws IllegalArgumentException
    {
        try
        {
            InputStream in = url.openStream();

            try
            {
                return read(in);
            }
            finally
            {
                in.close();
            }
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Could not read from \"" + url + "\"", e);
        }
    }

    /**
     * Creates a Document for the specified reader
     *
     * @param reader the reader
     * @return the document or null
     * @throws IOException if the stream could not be read
     * @throws IllegalArgumentException if the XML could not be parsed
     */
    public static Document read(Reader reader) throws IOException, IllegalArgumentException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new InputSource(reader));
        }
        catch (SAXException e)
        {
            throw new DOMIOException("Could not parse xml", e);
        }
        catch (ParserConfigurationException e)
        {
            throw new DOMIOException("Could not configure parser", e);
        }
    }

    /**
     * Creates a Document for the specified input stream
     *
     * @param in the input stream
     * @return the document or null
     * @throws IOException if the stream could not be read
     * @throws IllegalArgumentException if the XML could not be parsed
     */
    public static Document read(InputStream in) throws IOException, IllegalArgumentException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try
        {
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.parse(new InputSource(in));
        }
        catch (SAXException e)
        {
            throw new DOMIOException("Could not parse xml", e);
        }
        catch (ParserConfigurationException e)
        {
            throw new DOMIOException("Could not configure parser", e);
        }
    }

    /**
     * Transforms the specified node and returns the result as string
     *
     * @param node the node
     * @return the document as string
     * @throws IllegalArgumentException on any error
     */
    public static String toString(Node node) throws IllegalArgumentException
    {
        try
        {
            StringWriter writer = new StringWriter();

            try
            {
                write(node, writer);
            }
            finally
            {
                writer.close();
            }

            return writer.toString();
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Could not write transformer result");
        }
    }

    /**
     * Exports the specified document to the specified writer
     *
     * @param node the node
     * @param writer the writer
     * @throws IOException on occasion
     */
    public static void write(Node node, Writer writer) throws IOException
    {
        try
        {
            write(node, new StreamResult(writer));
        }
        catch (IllegalArgumentException e)
        {
            throw new DOMIOException("Failed to write node", e);
        }
    }

    /**
     * Exports the specified document to the specified result
     *
     * @param node the node
     * @param result the result
     * @throws IOException on occasion
     */
    public static void write(Node node, Result result) throws IOException
    {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer;
        try
        {
            transformer = factory.newTransformer();
        }
        catch (TransformerConfigurationException e)
        {
            throw new DOMIOException("Failed to create transformer", e);
        }

        transformer.setParameter("encoding", "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        try
        {
            transformer.transform(new DOMSource(node), result);
        }
        catch (TransformerException e)
        {
            throw new DOMIOException("Failed to transform node", e);
        }
    }

    /**
     * Returns the first element with the specified name
     *
     * @param root the node containing the children
     * @param name the name of the element
     * @return the node or null
     */
    public static Node element(Node root, String name)
    {
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i += 1)
        {
            Node node = childNodes.item(i);

            if ((node.getNodeType() == Node.ELEMENT_NODE) && (name.equals(node.getNodeName())))
            {
                return node;
            }
        }

        return null;
    }

    /**
     * Creates a list with all child elements with the specified name
     *
     * @param root the node containing the children
     * @param name the name of the element
     * @return an unmodifiable list of nodes, empty if none found
     */
    public static List<Node> list(Node root, String name)
    {
        List<Node> result = new ArrayList<>();
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i += 1)
        {
            Node node = childNodes.item(i);

            if ((node.getNodeType() == Node.ELEMENT_NODE) && (name.equals(node.getNodeName())))
            {
                result.add(node);
            }
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Finds the node matching the xPath
     *
     * @param root the node where to start searching
     * @param xPath the xPath
     * @return the node, null if not found
     */
    public static Node find(Node root, String xPath)
    {
        try
        {
            return (Node) (xPath(xPath).evaluate(root, XPathConstants.NODE));
        }
        catch (XPathExpressionException e)
        {
            throw new IllegalArgumentException("Invalid expression: " + xPath, e);
        }
    }

    /**
     * Finds all nodes matching the xPath
     *
     * @param root the node where to start searching
     * @param xPath the xPath
     * @return an unmodifiable list of all matching nodes, empty is none found
     */
    public static List<Node> findAll(Node root, String xPath)
    {
        List<Node> result = new ArrayList<>();
        NodeList childNodes;

        try
        {
            childNodes = (NodeList) (xPath(xPath).evaluate(root, XPathConstants.NODESET));
        }
        catch (XPathExpressionException e)
        {
            throw new IllegalArgumentException("Invalid expression: " + xPath, e);
        }

        for (int i = 0; i < childNodes.getLength(); i += 1)
        {
            result.add(childNodes.item(i));
        }

        return Collections.unmodifiableList(result);
    }

    /**
     * Creates an xPath expression
     *
     * @param xPath the xPath
     * @return the expression
     */
    public static XPathExpression xPath(String xPath)
    {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPathInstance = xPathFactory.newXPath();

        try
        {
            return xPathInstance.compile(xPath);
        }
        catch (XPathExpressionException e)
        {
            throw new IllegalArgumentException("Invalid xPath: " + xPath, e);
        }
    }

    /**
     * Returns all the text (#PCDATA) unter the specified node. The text will get trimmed.
     *
     * @param root the node containing the string
     * @param defaultValue the default value if not text was found
     * @return the text
     */
    public static String getText(Node root, String defaultValue)
    {
        if (root == null)
        {
            return defaultValue;
        }

        StringBuilder result = null;
        NodeList list = root.getChildNodes();

        for (int i = 0; i < list.getLength(); i += 1)
        {
            Node node = list.item(i);

            if ((Node.TEXT_NODE == node.getNodeType()) || (Node.CDATA_SECTION_NODE == node.getNodeType()))
            {
                if (result == null)
                {
                    result = new StringBuilder();
                }
                else
                {
                    result.append(" ");
                }

                result.append(node.getNodeValue());
            }
        }

        if (result == null)
        {
            return defaultValue;
        }

        String s = trim(result.toString());

        return (s.length() > 0) ? trim(s).trim() : defaultValue;
    }

    private static String trim(String text)
    {
        text = text.replace('\t', ' ');
        text = text.replace("\r\n", "\n");
        text = text.replace('\r', '\n');
        text = text.replaceAll("\\s+", " ");

        return text.trim();
    }

    /**
     * Returns the value of the attribute with the specified name.
     *
     * @param root the node with the attribute
     * @param name the name of the attribute
     * @return the value or the default value
     */
    public static String getAttribute(Node root, String name)
    {
        return getAttribute(root, name, null);
    }

    /**
     * Returns the value of the attribute with the specified name. If the value is not set, the default value will be
     * returned
     *
     * @param root the node with the attribute
     * @param name the name of the attribute
     * @param defaultValue the default value
     * @return the value or the default value
     */
    public static String getAttribute(Node root, String name, String defaultValue)
    {
        if (root == null)
        {
            return defaultValue;
        }

        NamedNodeMap map = root.getAttributes();
        String value = null;

        if (map != null)
        {
            Node node = map.getNamedItem(name);

            if (node != null)
            {
                value = node.getNodeValue();
            }
        }

        return (!Utils.isBlank(value)) ? value : defaultValue;
    }

    /**
     * Returns the value of the attribute with the specified name as Integer object. If the value is not set, the
     * default value will be returned.
     *
     * @param root the node with the attribute
     * @param name the name of the attribute
     * @param defaultValue the default value
     * @return the value as Integer object or the default value
     */
    public static Integer getIntegerAttribute(Node root, String name, Integer defaultValue)
    {
        String value = getAttribute(root, name);

        if ((value != null) && (value.length() > 0))
        {
            return Integer.valueOf(value);
        }

        return defaultValue;
    }

    /**
     * Returns the value of the attribute with the specified name as Double object. If the value is not set, the default
     * value will be returned.
     *
     * @param root the node with the attribute
     * @param name the name of the attribute
     * @param defaultValue the default value
     * @return the value as Double object or the default value
     */
    public static Double getDoubleAttribute(Node root, String name, Double defaultValue)
    {
        String value = getAttribute(root, name);

        if ((value != null) && (value.length() > 0))
        {
            return Double.valueOf(value);
        }

        return defaultValue;
    }

}
