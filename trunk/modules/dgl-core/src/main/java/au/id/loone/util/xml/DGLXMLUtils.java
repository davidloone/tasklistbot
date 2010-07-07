/*
 * Copyright 2007, David G Loone
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.id.loone.util.xml;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import au.id.loone.util.DGLRuntimeException;
import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * A utility class for creating an manipulating XML objects.
 *
 * @author David G Loone
 */
public class DGLXMLUtils
{

    // Logger for this class.
    @SuppressWarnings({"UnusedDeclaration"})
    private final static Logger LOG = TraceUtil.getLogger(DGLXMLUtils.class);

    /**
     * The class name of the standard SAX parser.
     */
    private static final String SAX_PARSER_CLASS = "org.apache.xerces.parsers.SAXParser";

    private static final DocumentBuilderFactory domBuilderFactory =
            DocumentBuilderFactory.newInstance();

    private static DocumentBuilder theirDOMBuilder = null;

    /**
     * Don't allow this class to be instantiated.
     */
    private DGLXMLUtils()
    {
        super();
    }

    /**
     * Creates a new (empty) XML DOM tree.
     *
     * @return
     *      The newly created DOM tree.
     */
    public static synchronized Document createDocument()
    {
        // The value to return.
        Document result;

        try {
            if (theirDOMBuilder == null) {
                theirDOMBuilder = domBuilderFactory.newDocumentBuilder();
            }
            result = theirDOMBuilder.newDocument();
        }
        catch (ParserConfigurationException e) {
            result = null;
        }
        return result;
    }

    /**
     * Creates a new (empty) XML DOM tree.
     *
     * @return
     *      The newly created DOM tree.
     */
    public static synchronized Document createDocument(
            final String namespaceURI,
            final String qualifiedName,
            final String publicId,
            final String systemId
    )
    {
        // The value to return.
        Document result;

        try {
            if (theirDOMBuilder == null) {
                theirDOMBuilder = domBuilderFactory.newDocumentBuilder();
            }
            result = theirDOMBuilder.getDOMImplementation().createDocument(namespaceURI,
                    qualifiedName, theirDOMBuilder.getDOMImplementation().createDocumentType(
                    qualifiedName, publicId, systemId));
        }
        catch (ParserConfigurationException e) {
            result = null;
        }

        return result;
    }

    /**
     * Create a new XML DOM tree from an XML input source.
     * The same local assumptions as for
     * {@link #createXMLReader(boolean)} are followed.
     *
     * @param in
     *      The XML input source to parse.
     * @param validate
     *      Determines whether the XML reader validates or not.
     *      A value of <code>true</code> validates the input document.
     * @return
     *      The newly created DOM tree.
     */
    public static Document createDocument(
            final InputSource in,
            final boolean validate
    )
            throws IOException, SAXException
    {
        LOG.trace("createDocument(" +
                TraceUtil.formatObj(in) + ", " +
                TraceUtil.formatObj(validate) + ")");

        // The value to return.
        final Document result;
        // The DOM parser.
        final DOMParser parser;

        parser = new DOMParser();
        parser.setEntityResolver(new DGLEntityResolver());
        parser.setProperty("http://apache.org/xml/properties/internal/entity-resolver", new DGLXMLEntityResolver());
        parser.setFeature("http://xml.org/sax/features/validation", validate);
        parser.setFeature("http://apache.org/xml/features/validation/schema", validate);
        parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", validate);
        if (validate) {
            parser.setErrorHandler(new StrictErrorHandler());
        }
        parser.parse(in);
        result = parser.getDocument();

        LOG.trace("~createDocument = ...");
        return result;
    }

    /**
     * Make a DOM into a string.
     */
    public static String toString(
            final Document document
    )
    {
        final DOMImplementationRegistry registry;
        try {
            registry = DOMImplementationRegistry.newInstance();
        }
        catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
        catch (final InstantiationException e) {
            throw new IllegalArgumentException(e);
        }
        catch (final IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        final DOMImplementationLS impl = (DOMImplementationLS)registry.getDOMImplementation("LS");

        final LSSerializer writer = impl.createLSSerializer();
        return writer.writeToString(document);
    }

    /**
     * Converts characters in a {@link String} into a format that can be embedded
     * into an attribute or a body of an XML element.
     *
     * <p>The following
     *      characters are currently converted:
     *      <table align="center" border="1" cellspacing="0">
     *          <tr>
     *              <th>from character</th>
     *              <th>to String</th>
     *          </tr>
     *          <tr>
     *              <td>&amp;</td>
     *              <td>&amp;amp;</td>
     *          </tr>
     *          <tr>
     *              <td>&lt;</td>
     *              <td>&amp;lt;</td>
     *          </tr>
     *          <tr>
     *              <td>&gt;</td>
     *              <td>&amp;gt;</td>
     *          </tr>
     *          <tr>
     *              <td>&quot;</td>
     *              <td>&amp;quot;</td>
     *          </tr>
     *          <tr>
     *              <td>'</td>
     *              <td>&amp;apos;</td>
     *          </tr>
     *      </table>
     *      In addition,
     *      characters with character codes of 127 and higher
     *      are converted to their appropriate numerical character entity references.</p>
     *
     * <p>Use org.apache.xml.serialize.XMLSerializer if you want to
     *      preserve \n, \t, etc.</p>
     *
     * @param text the text to convert
     * @return the converted text
     */
    @SuppressWarnings({"ConstantConditions", "UnusedDeclaration"})
    public static String serialize(
            final String text
    )
    {
        if (text == null) {
            return "";
        }

        final StringBuffer buf = new StringBuffer();
        final char[] c = text.toCharArray();

        for (char aC : c) {
            switch (aC) {
            case'&':
                buf.append("&amp;");
                break;
            case'\"':
                buf.append("&quot;");
                break;
            case'\'':
                buf.append("&apos;");
                break;
            case'<':
                buf.append("&lt;");
                break;
            case'>':
                buf.append("&gt;");
                break;
            default:
                if (aC >= 127) {
                    buf.append("&#");
                    buf.append((int)aC);
                    buf.append(";");
                }
                else {
                    buf.append(aC);
                }
                break;
            }
        }

        return buf.toString();
    }

    /**
     * Converts characters in a {@link String} into a format that can be embedded
     * into an attribute or a body of an HTML element.
     *
     * <p>The following
     *      characters are currently converted:
     *      <table align="center" border="1" cellspacing="0">
     *          <tr>
     *              <th>from character</th>
     *              <th>to String</th>
     *          </tr>
     *          <tr>
     *              <td>&amp;</td>
     *              <td>&amp;amp;</td>
     *          </tr>
     *          <tr>
     *              <td>&lt;</td>
     *              <td>&amp;lt;</td>
     *          </tr>
     *          <tr>
     *              <td>&gt;</td>
     *              <td>&amp;gt;</td>
     *          </tr>
     *          <tr>
     *              <td>&quot;</td>
     *              <td>&amp;quot;</td>
     *          </tr>
     *      </table>
     *      In addition,
     *      characters with character codes of 127 and higher
     *      are converted to their appropriate numerical character entity references.</p>
     *
     * <p>Use org.apache.xml.serialize.XMLSerializer if you want to
     *      preserve \n, \t, etc.</p>
     *
     * @param text the text to convert
     * @return the converted text
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static String serializeForHTML(
            final String text
    )
    {
        if (text == null) {
            return "";
        }

        final StringBuffer buf = new StringBuffer();
        final char[] c = text.toCharArray();

        for (char aC : c) {
            switch (aC) {
            case'&':
                buf.append("&amp;");
                break;
            case'\"':
                buf.append("&quot;");
                break;
            case'<':
                buf.append("&lt;");
                break;
            case'>':
                buf.append("&gt;");
                break;
            default:
                if (aC >= 127) {
                    buf.append("&#");
                    buf.append((int)aC);
                    buf.append(";");
                }
                else {
                    buf.append(aC);
                }
                break;
            }
        }

        return buf.toString();
    }

    /**
     * Create an XML reader with some default behaviour.
     *
     * <p>This default behaviour includes:
     *      <ul>
     *          <li>The {@link DGLEntityResolver entity resolver}.
     *              This class should be enhanced to include any new entity resolver behaviour
     *              that might be required within the context.</li>
     *          <li>If a validating parser is requested,
     *              then an strict error handler that reports all errors
     *              (by throwing the exception)
     *              is substituted for the default error handler.</li>
     *      </ul>
     * </p>
     *
     * @param validate
     *      Determines whether the resulting XML reader validates or not.
     *      A value of <code>true</code> produces a validating XML reader.
     * @return
     *      The XML reader for environment.
     */
    public static XMLReader createXMLReader(
            final boolean validate
    )
    {
        // The value to return.
        XMLReader result;

        // Create the reader.
        try {
            result = XMLReaderFactory.createXMLReader(SAX_PARSER_CLASS);
            result.setEntityResolver(new DGLEntityResolver());
            result.setFeature("http://xml.org/sax/features/validation", validate);
            if (validate) {
                result.setErrorHandler(new StrictErrorHandler());
            }
        }
        catch (SAXException e) {
            throw DGLRuntimeException.factory(e);
        }

        return result;
    }

    /**
     * Some DOM manipulation convenience utility methods.
     */
    public static class DOM
    {

        /**
         * Logger for this class.
         */
        @SuppressWarnings({"UnusedDeclaration"})
        private static final Logger LOG = TraceUtil.getLogger(DOM.class);

        /**
         * Don't allow this class to be instantiated.
         */
        private DOM()
        {
            super();
        }

        /**
         * Extract the text from a DOM node.
         * The node value of all child nodes that are of type {@link Text} are
         * appended together and returned.
         *
         * @param node
         *      The DOM node to extract text from.
         * @return
         *      The text of the node.
         */
        public static String extractText(
                final Node node
        )
        {
            // The value to return.
            StringBuffer result = new StringBuffer();
            // The child nodes.
            NodeList childNodes = node.getChildNodes();

            for (int i = 0; i < childNodes.getLength(); i++) {
                // This node.
                Node childNode = childNodes.item(i);
                if (childNode instanceof Text) {
                    result.append(childNode.getNodeValue());
                }
            }

            return result.toString();
        }

        /**
         * Get the child elements of a node.
         * This ignores any non-element child nodes.
         *
         * @param node
         *      The element node to extract the children from.
         * @return
         *      An iterator over the child elements.
         */
        public static Iterator childElements(
                final Node node
        )
        {
            // The list of eligible child nodes (ie, those that are elements).
            List<Node> resultList = new LinkedList<Node>();
            // The child node list.
            NodeList nodes = node.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) {
                Node thisNode = nodes.item(i);
                if (thisNode instanceof Element) {
                    resultList.add(thisNode);
                }
            }

            return resultList.iterator();
        }

        /**
         * Get the child elements of a node with a given tag name.
         * This ignores any non-element child nodes.
         *
         * @param node
         *      The element node to extract the children from.
         * @param tagName
         *      Just return elements with this tag name.
         * @return
         *      An iterator over the child elements.
         */
        public static Iterator childElements(
                final Node node,
                final String tagName
        )
        {
            // The list of eligible child nodes (ie, those that are elements).
            List<Node> resultList = new LinkedList<Node>();
            // The child node list.
            NodeList nodes = node.getChildNodes();

            for (int i = 0; i < nodes.getLength(); i++) {
                Node thisNode = nodes.item(i);
                if ((thisNode instanceof Element) &&
                        ((Element)thisNode).getTagName().equals(tagName)) {
                    resultList.add(thisNode);
                }
            }

            return resultList.iterator();
        }

    }

    /**
     * An error handler that reports all errors, warnings, etc
     * by throwing them as exceptions.
     */
    public static class StrictErrorHandler
            implements ErrorHandler
    {

        public StrictErrorHandler()
        {
            super();
        }

        /**
         * Receive a parsing warning, and throw it as an exception.
         */
        public void warning(
                final SAXParseException e
        )
                throws SAXException
        {
            throw e;
        }

        /**
         * Receive a parsing error, and throw it as an exception.
         */
        public void error(
                final SAXParseException e
        )
                throws SAXException
        {
            throw e;
        }

        /**
         * Receive a fatal parsing error, and throw it as an exception.
         */
        public void fatalError(
                final SAXParseException e
        )
                throws SAXException
        {
            throw e;
        }

    }

    /**
     * A node iterator class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static class NodeIterator
            implements Iterator
    {

        /**
         * The node list.
         */
        private NodeList itsNodeList;

        /**
         * The counter.
         */
        private int itsCounter;

        /**
         * Make a new node iterator.
         */
        public NodeIterator(
                final NodeList nodeList
        )
        {
            itsNodeList = nodeList;
            itsCounter = 0;
        }

        /**
         */
        public boolean hasNext()
        {
            return itsCounter == (itsNodeList.getLength() - 1);
        }

        /**
         */
        public Object next()
        {
            return itsNodeList.item(itsCounter++);
        }

        /**
         * @exception UnsupportedOperationException
         *      Always thrown.
         */
        public void remove()
        {
            throw new UnsupportedOperationException();
        }

    }

}