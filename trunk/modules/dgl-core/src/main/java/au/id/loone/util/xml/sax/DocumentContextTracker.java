package au.id.loone.util.xml.sax;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Keeps track of the current context within a document.
 *
 * <p>This class is constructed in such a way
 *      that it is possible to have a single context tracker
 *      for an entire document,
 *      and an arbitrary number of filters can use it.
 *      When a filter wants to keep track of a local path,
 *      then a slave is created using the {@link #createSlave()} method.</p>
 *
 * <h3>JavaBean Properties</h3>
 *
 * <table align="center" border="1" cellpadding="3" cellspacing="0" width="90%">
 *      <tr>
 *          <th>name</th>
 *          <th>set</th>
 *          <th>get</th>
 *          <th>description</th>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>currentPath</b></td>
 *          <td align="center" valign="top">&nbsp;</td>
 *          <td align="center" valign="top">{@linkplain #getCurrentPath() get}</td>
 *          <td align="left" valign="top">The current global path in the document.</td>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>localPath</b></td>
 *          <td align="center" valign="top">&nbsp;</td>
 *          <td align="center" valign="top">{@linkplain #getLocalPath() get}</td>
 *          <td align="left" valign="top">The current path within the fragment.</td>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>prefixMappings</b></td>
 *          <td align="center" valign="top">&nbsp;</td>
 *          <td align="center" valign="top">{@linkplain #getPrefixMappings() get}</td>
 *          <td align="left" valign="top">The XML namespace prefixes that are currently in effect.</td>
 *      </tr>
 * </table>
 *
 * @author David G Loone
 */
public class DocumentContextTracker
        extends DefaultHandler
        implements ContentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(DocumentContextTracker.class);

    /**
     * The current path.
     */
    private List<Element> currentPath;

    /**
     * Chached readonly current path.
     */
    private List<Element> currentPathReadonly;

    /**
     * Current value of the <b>prefixMappings</b> property.
     */
    private Map<String, String> prefixMappings;

    /**
     */
    public DocumentContextTracker()
    {
        super();

        currentPath = new LinkedList<Element>();
        prefixMappings = new HashMap<String, String>();
    }

    /**
     * Constructor for use by a slave.
     */
    private DocumentContextTracker(
            final DocumentContextTracker master
    )
    {
        super();

        // The global path tracks the global path of the master.
        currentPath = master.currentPath;

        // The prefix mappings forks from the current value in the master.
        prefixMappings = new HashMap<String, String>(master.prefixMappings);
    }

    /**
     * Create a slave document context tracker,
     * whose local path is reset at the current position.
     */
    public DocumentContextTracker createSlave()
    {
        return new Slave(this);
    }

    /**
     */
    @Override
    public void startElement(
            final String uri,
            final String localName,
            final String qName,
            final Attributes attributes
    )
            throws SAXException
    {
        // Add the new element to the end of the path.
        currentPath.add(new Element(uri, localName, qName, attributes));
        currentPathReadonly = null;
    }

    /**
     */
    @Override
    public void endElement(
            final String uri,
            final String localName,
            final String qName
    )
            throws SAXException
    {
        // Remove the last element.
        currentPath.remove(currentPath.size() - 1);
        currentPathReadonly = null;
    }

    /**
     */
    @Override
    public void startPrefixMapping(
            final String prefix,
            final String uri
    )
    {
        if (prefixMappings.containsKey(prefix)) {
            throw new UnsupportedOperationException("Overriding existing prefix is not supported.");
        }
        prefixMappings.put(prefix, uri);
    }

    /**
     */
    @Override
    public void endPrefixMapping(
            final String prefix
    )
    {
        prefixMappings.remove(prefix);
    }

    /**
     * Getter method for the <b>currentPath</b> property.
     */
    public List<Element> getCurrentPath() {
        if (currentPathReadonly == null) {
            currentPathReadonly = Collections.unmodifiableList(currentPath);
        }

        return currentPathReadonly;
    }

    /**
     * Getter method for the <b>localPath</b> property.
     */
    public List<Element> getLocalPath() {
        return getCurrentPath();
    }

    /**
     * Getter method for the <b>prefixMappings</b> property.
     */
    public Map<String, String> getPrefixMappings() {return Collections.unmodifiableMap(prefixMappings);}

    /**
     */
    private class Slave
            extends DocumentContextTracker
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private final Logger LOG = TraceUtil.getLogger(Slave.class);

        /**
         * Current value of the <b>localPathIndex</b> property.
         */
        private int localPathIndex;

        /**
         */
        public Slave(
                final DocumentContextTracker master
        )
        {
            super(master);

            this.localPathIndex = master.currentPath.size();
        }

        /**
         */
        public String toString()
        {
            return Slave.class.getName() + "{" +
                    super.toString() + ", " +
                    TraceUtil.formatObj(currentPath, "currentPath") + ", " +
                    TraceUtil.formatObj(localPathIndex, "localPathIndex") + "}";
        }

        /**
         * Getter method for the <b>currentPath</b> property.
         */
        public List<Element> getCurrentPath() {
            return DocumentContextTracker.this.getCurrentPath();
        }

        /**
         * Getter method for the <b>localPath</b> property.
         */
        public List<Element> getLocalPath() {
            return (localPathIndex > currentPath.size()) ? null :
                    Collections.unmodifiableList(currentPath.subList(localPathIndex, currentPath.size()));
        }

    }

    /**
     * An element in the path to the current location.
     *
     * <h3>JavaBean Properties</h3>
     *
     * <table align="center" border="1" cellpadding="3" cellspacing="0" width="90%">
     *      <tr>
     *          <th>name</th>
     *          <th>set</th>
     *          <th>get</th>
     *          <th>description</th>
     *      </tr>
     *      <tr>
     *          <td align="left" valign="top"><b>attributes</b></td>
     *          <td align="center" valign="top">&nbsp;</td>
     *          <td align="center" valign="top">{@linkplain #getAttributes() get}</td>
     *          <td align="left" valign="top">The attributes that set on the element
     *              as per {@link ContentHandler#startElement(String, String, String, Attributes)}.</td>
     *      </tr>
     *      <tr>
     *          <td align="left" valign="top"><b>localName</b></td>
     *          <td align="center" valign="top">&nbsp;</td>
     *          <td align="center" valign="top">{@linkplain #getLocalName() get}</td>
     *          <td align="left" valign="top">See {@link ContentHandler#startElement(String, String, String, Attributes)}.</td>
     *      </tr>
     *      <tr>
     *          <td align="left" valign="top"><b>qName</b></td>
     *          <td align="center" valign="top">&nbsp;</td>
     *          <td align="center" valign="top">{@linkplain #getQName() get}</td>
     *          <td align="left" valign="top">See {@link ContentHandler#startElement(String, String, String, Attributes)}.</td>
     *      </tr>
     *      <tr>
     *          <td align="left" valign="top"><b>uri</b></td>
     *          <td align="center" valign="top">&nbsp;</td>
     *          <td align="center" valign="top">{@linkplain #getUri() get}</td>
     *          <td align="left" valign="top">See {@link ContentHandler#startElement(String, String, String, Attributes)}.</td>
     *      </tr>
     * </table>
     *
     * @author David G Loone
     */
    public static class Element
    {

        @SuppressWarnings({"UnusedDeclaration"})
        private static final Logger LOG = TraceUtil.getLogger(Element.class);

        /**
         * Current value of the <b>attributes</b> property.
         */
        private Attributes attributes;

        /**
         * Current value of the <b>localName</b> property.
         */
        private String localName;

        /**
         * Current value of the <b>qName</b> property.
         */
        private String qName;

        /**
         * Current value of the <b>uri</b> property.
         */
        private String uri;

        /**
         */
        private Element()
        {
            super();
        }

        /**
         */
        public Element(
                final String uri,
                final String localName
        )
        {
            this();

            this.localName = localName;
            this.uri = uri;
        }

        /**
         */
        public Element(
                final String uri,
                final String localName,
                final String qName,
                final Attributes attributes
        )
        {
            this();

            this.attributes = attributes;
            this.localName = localName;
            this.qName = qName;
            this.uri = uri;
        }

        /**
         */
        public int hashCode()
        {
            return DGLUtil.hashCode(uri) +
                    DGLUtil.hashCode(localName);
        }

        /**
         */
        public boolean equals(
                final Object otherObj
        )
        {
            final boolean result;

            if (otherObj == this) {
                result = true;
            }
            else if (otherObj == null) {
                result = false;
            }
            else if (otherObj instanceof Element) {
                final Element other = (Element)otherObj;
                result = DGLUtil.equals(uri, other.uri) &&
                        DGLUtil.equals(localName, other.localName);
            }
            else {
                result = false;
            }

            return result;
        }

        /**
         */
        public String toString()
        {
            final StringBuilder buf = new StringBuilder();
            buf.append("{");
            buf.append(uri);
            buf.append("}");
            buf.append(localName);
            buf.append("[");
            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    if (i != 0) {
                        buf.append(" ");
                    }
                    buf.append(attributes.getLocalName(i));
                    buf.append("=\"");
                    buf.append(attributes.getValue(i));
                    buf.append("\"");
                }
            }
            buf.append("]");

            return buf.toString();
        }

        /**
         * Getter method for the <b>attributes</b> property.
         */
        public Attributes getAttributes() {return attributes;}

        /**
         * Getter method for the <b>localName</b> property.
         */
        public String getLocalName() {return localName;}

        /**
         * Getter method for the <b>qName</b> property.
         */
        public String getQName() {return qName;}

        /**
         * Getter method for the <b>uri</b> property.
         */
        public String getUri() {return uri;}

    }

}