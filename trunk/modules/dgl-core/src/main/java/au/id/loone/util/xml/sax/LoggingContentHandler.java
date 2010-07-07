package au.id.loone.util.xml.sax;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A SAX content handler that just logs everything.
 *
 * @author David G Loone
 */
public final class LoggingContentHandler
        implements ContentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(LoggingContentHandler.class);

    /**
     */
    public LoggingContentHandler()
    {
        super();
    }

    /**
     */
    public void setDocumentLocator(
            final Locator locator
    )
    {
        LOG.trace("setDocumentLocator(" + TraceUtil.formatObj(locator) + ")");
    }

    /**
     */
    public void startDocument()
            throws SAXException
    {
        LOG.trace("startDocument()");
    }

    /**
     */
    public void endDocument()
            throws SAXException
    {
        LOG.trace("endDocument()");
    }

    /**
     */
    public void startPrefixMapping(
            final String prefix,
            final String url
    )
            throws SAXException
    {
        LOG.trace("startPrefixMapping(" +
                TraceUtil.formatObj(prefix) + ", " +
                TraceUtil.formatObj(url) + ")");
    }

    /**
     */
    public void endPrefixMapping(
            final String prefix
    )
            throws SAXException
    {
        LOG.trace("endPrefixMapping(" +
                TraceUtil.formatObj(prefix) + ")");
    }

    /**
     */
    public void startElement(
            final String uri,
            final String localName,
            final String qName,
            final Attributes attributes
    )
            throws SAXException
    {
        LOG.trace("startElement(" +
                TraceUtil.formatObj(uri) + ", " +
                TraceUtil.formatObj(localName) + ", " +
                TraceUtil.formatObj(qName) + ", " +
                TraceUtil.formatObj(attributes) + ")");
    }

    /**
     */
    public void endElement(
            final String uri,
            final String localName,
            final String qName
    )
            throws SAXException
    {
        LOG.trace("endElement(" +
                TraceUtil.formatObj(uri) + ", " +
                TraceUtil.formatObj(localName) + ", " +
                TraceUtil.formatObj(qName) + ")");
    }

    /**
     */
    public void characters(
            final char[] chars,
            final int start,
            final int length
    )
            throws SAXException
    {
        LOG.trace("characters: " + TraceUtil.formatObj(new String(chars, start, length)));
    }

    /**
     */
    public void ignorableWhitespace(
            final char[] chars,
            final int start,
            final int length
    )
            throws SAXException
    {
        LOG.trace("ignorableWhitespace(" +
                TraceUtil.formatObj(chars) + ", " +
                TraceUtil.formatObj(start) + ", " +
                TraceUtil.formatObj(length) + ")");
    }

    /**
     */
    public void processingInstruction(
            final String target,
            final String data
    )
            throws SAXException
    {
        LOG.trace("processingIntruction(" +
                TraceUtil.formatObj(target) + ", " +
                TraceUtil.formatObj(data) + ")");
    }

    /**
     */
    public void skippedEntity(
            final String name
    )
            throws SAXException
    {
        LOG.trace("skippedEntry(" +
                TraceUtil.formatObj(name) + ")");
    }

}