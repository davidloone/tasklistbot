package au.id.loone.util.xml.sax;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An abstract default fragment handler that implements the required methods but does nothing.
 *
 * @author David G Loone
 */
public abstract class FragmentHandlerImpl
        implements FragmentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(FragmentHandlerImpl.class);

    /**
     */
    public FragmentHandlerImpl()
    {
        super();
    }

    /**
     */
    public void startFragment(
            final DocumentContextTracker context,
            final Attributes attributes
    )
            throws SAXException
    {
        // Do nothing.
    }

    /**
     */
    public void endFragment(
            final DocumentContextTracker context
    )
            throws SAXException
    {
        // Do nothing.
    }

    /**
     */
    public void startElement(
            final DocumentContextTracker context,
            final String uri,
            final String localName,
            final String qName,
            final Attributes attributes
    )
            throws SAXException
    {
        // Do nothing.
    }

    /**
     */
    public void endElement(
            final DocumentContextTracker context,
            final String uri,
            final String localName,
            final String qName
    )
            throws SAXException
    {
        // Do nothing.
    }

    /**
     */
    public void characters(
            final DocumentContextTracker context,
            final char[] chars,
            final int start,
            final int length
    )
            throws SAXException
    {
        // Do nothing.
    }

    /**
     */
    public void ignorableWhitespace(
            final DocumentContextTracker context,
            final char[] chars,
            final int start,
            final int length
    )
            throws SAXException
    {
        // Do nothing.
    }

    /**
     */
    public void processingInstruction(
            final DocumentContextTracker context,
            final String target,
            final String data
    )
            throws SAXException
    {
        // Do nothing.
    }

    /**
     */
    public void skippedEntity(
            final DocumentContextTracker context,
            final String name
    )
            throws SAXException
    {
        // Do nothing.
    }

}