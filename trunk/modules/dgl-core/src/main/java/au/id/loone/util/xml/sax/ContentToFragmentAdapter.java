package au.id.loone.util.xml.sax;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;

/**
 * Acts as a {@linkplain ContentHandler content handler},
 * but passes events on to a {@linkplain FragmentHandler fragment handler},
 * as if the entire document is the fragment.
 *
 * @author David G Loone
 */
public class ContentToFragmentAdapter
        implements ContentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(ContentToFragmentAdapter.class);

    /**
     * Current value of the <b>fragmentHandler</b> property.
     */
    private FragmentHandler fragmentHandler;

    /**
     * Current value of the <b>context</b> property.
     */
    private DocumentContextTracker context;

    /**
     * Current value of the <b>isInDocument</b> property.
     */
    private boolean isInDocument;

    /**
     */
    public ContentToFragmentAdapter()
    {
        super();

        isInDocument = false;
        context = new DocumentContextTracker();
    }

    /**
     */
    public ContentToFragmentAdapter(
            final FragmentHandler fragmentHandler
    )
    {
        this();

        this.fragmentHandler = fragmentHandler;
    }

    /**
     */
    public void setDocumentLocator(
            final Locator locator
    )
    {
        context.setDocumentLocator(locator);
    }

    /**
     */
    public void startDocument()
            throws SAXException
    {
        context.startDocument();
    }

    /**
     */
    public void endDocument()
            throws SAXException
    {
        context.endDocument();
    }

    /**
     */
    public void startPrefixMapping(
            final String prefix,
            final String uri
    )
            throws SAXException
    {
        context.startPrefixMapping(prefix, uri);
    }

    /**
     */
    public void endPrefixMapping(
            final String prefix
    )
            throws SAXException
    {
        context.endPrefixMapping(prefix);
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
        context.startElement(uri, localName, qName, attributes);
        if (isInDocument) {
            fragmentHandler.startElement(context, uri, localName, qName, attributes);
        }
        else {
            fragmentHandler.startFragment(context, attributes);
            isInDocument = true;
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
        context.endElement(uri, localName, qName);
        if (isInDocument) {
            fragmentHandler.endElement(context, uri, localName, qName);
        }
        else {
            isInDocument = false;
            fragmentHandler.endFragment(context);
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
        context.characters(chars, start, length);
        if (isInDocument) {
            fragmentHandler.characters(context,  chars, start, length);
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
        context.ignorableWhitespace(chars, start, length);
        if (isInDocument) {
            fragmentHandler.ignorableWhitespace(context,  chars, start, length);
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
        context.processingInstruction(target, data);
        if (isInDocument) {
            fragmentHandler.processingInstruction(context, target, data);
        }
    }

    /**
     */
    public void skippedEntity(
            final String name
    )
            throws SAXException
    {
        context.skippedEntity(name);
        if (isInDocument) {
            fragmentHandler.skippedEntity(context, name);
        }
    }

    /**
     * Setter for the <b>fragmentHandler</b> property.
     */
    public void setFragmentHandler(final FragmentHandler fragmentHandler) {this.fragmentHandler = fragmentHandler;}

    /**
     * Getter method for the <b>fragmentHandler</b> property.
     */
    public FragmentHandler getFragmentHandler() {return fragmentHandler;}

    /**
     * Getter method for the <b>context</b> property.
     */
    public DocumentContextTracker getContext() {return context;}

    /**
     * Getter method for the <b>isInDocument</b> property.
     */
    public boolean getIsInDocument() {return isInDocument;}

}