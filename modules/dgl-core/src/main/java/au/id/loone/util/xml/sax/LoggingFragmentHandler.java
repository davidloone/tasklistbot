package au.id.loone.util.xml.sax;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A SAX fragment content handler that just logs everything.
 *
 * @author David G Loone
 */
public final class LoggingFragmentHandler
        implements FragmentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(LoggingFragmentHandler.class);

    /**
     */
    public LoggingFragmentHandler()
    {
        super();
    }

    /**
     */
    public void startFragment(
            final DocumentContextTracker context,
            final Attributes attributes
    )
    {
        LOG.trace("startFragment(" +
                TraceUtil.formatObj(context) + ", " +
                TraceUtil.formatObj(attributes) + ")");
        LOG.trace("startFragment: " + TraceUtil.formatObj(context, "context.currentPath"));
        LOG.trace("startFragment: " + TraceUtil.formatObj(context, "context.localPath"));
    }

    /**
     */
    public void endFragment(
            final DocumentContextTracker context
    )
    {
        LOG.trace("endFragment(" + TraceUtil.formatObj(context) + ")");
        LOG.trace("endFragment: " + TraceUtil.formatObj(context, "context.currentPath"));
        LOG.trace("endFragment: " + TraceUtil.formatObj(context, "context.localPath"));
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
        LOG.trace("startElement(" +
                TraceUtil.formatObj(context) + ", " +
                TraceUtil.formatObj(uri) + ", " +
                TraceUtil.formatObj(localName) + ", " +
                TraceUtil.formatObj(qName) + ", " +
                TraceUtil.formatObj(attributes) + ")");
        LOG.trace("startElement: " + TraceUtil.formatObj(context, "context.currentPath"));
        LOG.trace("startElement: " + TraceUtil.formatObj(context, "context.localPath"));
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
        LOG.trace("endElement(" +
                TraceUtil.formatObj(context) + ", " +
                TraceUtil.formatObj(uri) + ", " +
                TraceUtil.formatObj(localName) + ", " +
                TraceUtil.formatObj(qName) + ")");
        LOG.trace("endElement: " + TraceUtil.formatObj(context, "context.currentPath"));
        LOG.trace("endElement: " + TraceUtil.formatObj(context, "context.localPath"));
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
        LOG.trace("characters: " + TraceUtil.formatObj(new String(chars, start, length)));
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
        LOG.trace("ignorableWhitespace(" +
                TraceUtil.formatObj(context) + ", " +
                TraceUtil.formatObj(chars) + ", " +
                TraceUtil.formatObj(start) + ", " +
                TraceUtil.formatObj(length) + ")");
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
        LOG.trace("processingIntruction(" +
                TraceUtil.formatObj(context) + ", " +
                TraceUtil.formatObj(target) + ", " +
                TraceUtil.formatObj(data) + ")");
    }

    /**
     */
    public void skippedEntity(
            final DocumentContextTracker context,
            final String name
    )
            throws SAXException
    {
        LOG.trace("skippedEntry(" +
                TraceUtil.formatObj(context) + ", " +
                TraceUtil.formatObj(name) + ")");
    }

}