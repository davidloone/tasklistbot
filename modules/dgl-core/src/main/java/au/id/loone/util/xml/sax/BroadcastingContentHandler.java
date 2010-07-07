package au.id.loone.util.xml.sax;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * A SAX content handler that broadcasts all SAX events to a number of other content handlers.
 *
 * @author David G Loone
 */
public final class BroadcastingContentHandler
        implements ContentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(BroadcastingContentHandler.class);

    /**
     * Current value of the <b>contentHandlers</b> property.
     */
    private ContentHandler[] contentHandlers;

    /**
     */
    public BroadcastingContentHandler()
    {
        super();
    }

    /**
     */
    public BroadcastingContentHandler(
            final ContentHandler ... contentHandlers
    )
    {
        this();

        this.contentHandlers = contentHandlers;
    }

    /**
     */
    public void setDocumentLocator(
            final Locator locator
    )
    {
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.setDocumentLocator(locator);
            }
        }
    }

    /**
     */
    public void startDocument()
            throws SAXException
    {
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.startDocument();
            }
        }
    }

    /**
     */
    public void endDocument()
            throws SAXException
    {
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.endDocument();
            }
        }
    }

    /**
     */
    public void startPrefixMapping(
            final String prefix,
            final String url
    )
            throws SAXException
    {
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.startPrefixMapping(prefix, url);
            }
        }
    }

    /**
     */
    public void endPrefixMapping(
            final String prefix
    )
            throws SAXException
    {
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.endPrefixMapping(prefix);
            }
        }
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
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.startElement(uri, localName, qName, attributes);
            }
        }
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
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.endElement(uri, localName, qName);
            }
        }
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
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.characters(chars, start, length);
            }
        }
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
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.ignorableWhitespace(chars, start, length);
            }
        }
    }

    /**
     */
    public void processingInstruction(
            final String target,
            final String data
    )
            throws SAXException
    {
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.processingInstruction(target, data);
            }
        }
    }

    /**
     */
    public void skippedEntity(
            final String name
    )
            throws SAXException
    {
        if (contentHandlers != null) {
            for (final ContentHandler contentHandler : contentHandlers) {
                contentHandler.skippedEntity(name);
            }
        }
    }

    /**
     * Setter for the <b>contentHandlers</b> property.
     */
    public void setContentHandlers(final ContentHandler[] contentHandlers) {this.contentHandlers = contentHandlers;}

    /**
     * Getter method for the <b>contentHandlers</b> property.
     */
    public ContentHandler[] getContentHandlers() {return contentHandlers;}

}