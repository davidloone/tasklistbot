package au.id.loone.util.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Defines the functionality required for a fragment handler.
 *
 * <p>A fragment handler is similar to a SAX {@linkplain org.xml.sax.ContentHandler content handler},
 *      except it is aware of fragments.
 *      Specifically,
 *      the changes are:</p>
 * <ul>
 *      <li>Each method accepts an instance of
 *          {@link DocumentContextTracker},
 *          which allows the fragment handler to know its current location in the document.</li>
 *      <li>The addition of the
 *          {@link #startFragment(DocumentContextTracker, Attributes)} and
 *          {@link #endFragment(DocumentContextTracker)} methods
 *          to mark the start and end of the fragment respectively.</li>
 * </ul>
 *
 * @author David G Loone
 */
public interface FragmentHandler
{

    /**
     * Flags the start of a fragment.
     * This is usually an element.
     * The element itself is not part of the fragment,
     * but the attributes on that element are.
     *
     * @param context
     *      The location within the document of the fragment.
     * @param attributes
     *      The attributes on the element.
     */
    public void startFragment(
            DocumentContextTracker context,
            Attributes attributes
    )
            throws SAXException;

    /**
     * Flags the end of the fragment.
     *
     * @param context
     *      The location within the document of the fragment
     *      (after the fragment).
     */
    public void endFragment(
            DocumentContextTracker context
    )
            throws SAXException;

    /**
     * @param context
     *      The current location within the document.
     *
     * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
     */
    public void startElement(
            DocumentContextTracker context,
            String uri,
            String localName,
            String qName,
            Attributes attributes
    )
            throws SAXException;

    /**
     * @param context
     *      The current location within the document.
     *
     * @see org.xml.sax.ContentHandler#endElement(String, String, String)
     */
    public void endElement(
            DocumentContextTracker context,
            String uri,
            String localName,
            String qName
    )
            throws SAXException;

    /**
     * @param context
     *      The current location within the document.
     *
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(
            DocumentContextTracker context,
            char[] chars,
            int start,
            int length
    )
            throws SAXException;

    /**
     * @param context
     *      The current location within the document.
     *
     * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
     */
    public void ignorableWhitespace(
            DocumentContextTracker context,
            char[] chars,
            int start,
            int length
    )
            throws SAXException;

    /**
     * @param context
     *      The current location within the document.
     *
     * @see org.xml.sax.ContentHandler#processingInstruction(String, String)
     */
    public void processingInstruction(
            DocumentContextTracker context,
            String target,
            String data
    )
            throws SAXException;

    /**
     * @param context
     *      The current location within the document.
     *
     * #see org.xml.sax.ContentHandler#skippedEntity(String)
     */
    public void skippedEntity(
            DocumentContextTracker context,
            String name
    )
            throws SAXException;

}