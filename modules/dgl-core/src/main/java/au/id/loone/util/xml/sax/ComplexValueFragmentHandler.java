package au.id.loone.util.xml.sax;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

/**
 * A SAX fragment handler for complex content.
 * It broadcasts all SAX fragment events to a number of other fragment handlers.
 *
 * <h3>JavaBean Properties</h3>
 *
 * <table align="center" border="1" cellpadding="3" cellspacing="0" width="100%">
 *      <tr>
 *          <th>name</th>
 *          <th>set</th>
 *          <th>get</th>
 *          <th>description</th>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>fragmentHandlers</b></td>
 *          <td align="center" valign="top">{@linkplain #setFragmentHandlers(FragmentHandler[]) set}</td>
 *          <td align="center" valign="top">{@linkplain #getFragmentHandlers() get}</td>
 *          <td alilgn="left" valign="top">The fragment handlers to call inside the fragment.</td>
 *      </tr>
 * </table>
 *
 * @author David G Loone
 */
public class ComplexValueFragmentHandler
        implements FragmentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(ComplexValueFragmentHandler.class);

    /**
     * Current value of the <b>fragmentHandlers</b> property.
     */
    private FragmentHandler[] fragmentHandlers;

    /**
     */
    public ComplexValueFragmentHandler()
    {
        super();
    }

    /**
     */
    public ComplexValueFragmentHandler(
            final FragmentHandler ... fragmentHandlers
    )
    {
        this();

        this.fragmentHandlers = fragmentHandlers;
    }

    /**
     */
    public void startFragment(
            final DocumentContextTracker context,
            final Attributes attributes
    )
            throws SAXException
    {
        if (fragmentHandlers != null) {
            for (final FragmentHandler fragmentHandler : fragmentHandlers) {
                fragmentHandler.startFragment(context, attributes);
            }
        }
    }

    /**
     */
    public void endFragment(
            final DocumentContextTracker context
    )
            throws SAXException
    {
        if (fragmentHandlers != null) {
            for (final FragmentHandler fragmentHandler : fragmentHandlers) {
                fragmentHandler.endFragment(context);
            }
        }
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
        if (fragmentHandlers != null) {
            for (final FragmentHandler fragmentHandler : fragmentHandlers) {
                fragmentHandler.startElement(context, uri, localName, qName, attributes);
            }
        }
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
        if (fragmentHandlers != null) {
            for (final FragmentHandler fragmentHandler : fragmentHandlers) {
                fragmentHandler.endElement(context, uri, localName, qName);
            }
        }
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
        if (fragmentHandlers != null) {
            for (final FragmentHandler fragmentHandler : fragmentHandlers) {
                fragmentHandler.characters(context, chars, start, length);
            }
        }
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
        if (fragmentHandlers != null) {
            for (final FragmentHandler fragmentHandler : fragmentHandlers) {
                fragmentHandler.ignorableWhitespace(context, chars, start, length);
            }
        }
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
        if (fragmentHandlers != null) {
            for (final FragmentHandler fragmentHandler : fragmentHandlers) {
                fragmentHandler.processingInstruction(context, target, data);
            }
        }
    }

    /**
     */
    public void skippedEntity(
            final DocumentContextTracker context,
            final String name
    )
            throws SAXException
    {
        if (fragmentHandlers != null) {
            for (final FragmentHandler fragmentHandler : fragmentHandlers) {
                fragmentHandler.skippedEntity(context, name);
            }
        }
    }

    /**
     * Setter for the <b>fragmentHandlers</b> property.
     */
    public void setFragmentHandlers(final FragmentHandler[] fragmentHandlers) {this.fragmentHandlers = fragmentHandlers;}

    /**
     * Getter method for the <b>fragmentHandlers</b> property.
     */
    public FragmentHandler[] getFragmentHandlers() {return fragmentHandlers;}

}