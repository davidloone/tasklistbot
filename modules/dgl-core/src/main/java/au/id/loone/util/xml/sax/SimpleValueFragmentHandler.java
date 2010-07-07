package au.id.loone.util.xml.sax;

import java.util.LinkedList;
import java.util.List;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Base class for content handlers that extract a simple value from an element.
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
 *          <td align="left" valign="top"><b>value</b></td>
 *          <td align="center" valign="top">&nbsp;</td>
 *          <td align="center" valign="top">{@linkplain #getValue() get}</td>
 *          <td alilgn="left" valign="top">The value derived from the element content.
 *              Specifically,
 *              this is the value returned by the last call to {@link #valueFactory(String)}.</td>
 *      </tr>
 *      <tr>
 *          <td align="left" valign="top"><b>values</b></td>
 *          <td align="center" valign="top">&nbsp;</td>
 *          <td align="center" valign="top">{@linkplain #getValues() get}</td>
 *          <td alilgn="left" valign="top">All values of elements in the parent fragment.</td>
 *      </tr>
 * </table>
 *
 * @author David G Loone
 */
public abstract class SimpleValueFragmentHandler<T>
        extends FragmentHandlerImpl
        implements FragmentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(SimpleValueFragmentHandler.class);

    /**
     * The content buffer.
     */
    private StringBuilder contentBuf;

    /**
     * Current value of the <b>value</b> property.
     */
    private T value;

    /**
     * Current value of the <b>values</b> property.
     */
    private List<T> values = new LinkedList<T>();

    /**
     */
    public SimpleValueFragmentHandler()
    {
        super();
    }

    /**
     * Clears the value.
     */
    public void clear()
    {
        value = null;
        values = new LinkedList<T>();
    }

    /**
     * Called exactly once when the element content has been found,
     * and must translate that element content into the desired data type.
     *
     * <p>Concrete implementations of this class must implement this method
     *      to perform the appropriate type conversion.</p>
     *
     * @param content
     *      The element content.
     * @return
     *      The data value corresponding to the element content.
     */
    public abstract T valueFactory(
            final String content
    );

    /**
     */
    public void startFragment(
            final DocumentContextTracker context,
            final Attributes attributes
    )
            throws SAXException
    {
        contentBuf = new StringBuilder();
    }

    /**
     */
    public void endFragment(
            final DocumentContextTracker context
    )
            throws SAXException
    {
        try {
            value = valueFactory(contentBuf.toString());
        }
        catch (final RuntimeException e) {
            throw new SAXException("Invalid primitive element content at " +
                    TraceUtil.formatObj(context.getCurrentPath()), e);
        }
        values.add(value);
        contentBuf = null;
    }

    /**
     * @throws IllegalArgumentException
     *      Always thrown, since any element inside the content means it isn't a simple value after all.
     */
    public void startElement(
            final DocumentContextTracker context,
            final String uri,
            final String localName,
            final String qName,
            final Attributes attributes
    )
            throws SAXException, IllegalArgumentException
    {
        // New element within the fragment. The element obviously doesn't contain a primitive.
        throw new IllegalArgumentException("Element does not contain simple content: " +
                TraceUtil.formatObj(context.getCurrentPath()));
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
        contentBuf.append(new String(chars, start, length));
    }

    /**
     * Getter method for the <b>value</b> property.
     */
    public T getValue() {return value;}

    /**
     * Getter method for the <b>values</b> property.
     */
    public List<T> getValues() {return values;}

}