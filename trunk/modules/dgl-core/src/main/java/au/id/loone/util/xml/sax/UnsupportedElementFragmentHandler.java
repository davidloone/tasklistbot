package au.id.loone.util.xml.sax;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * A fragment handler that can be used for an unsupported element.
 * It simply throws an exception if the fragment is encountered.
 *
 * @author David G Loone
 */
public final class UnsupportedElementFragmentHandler
        extends FragmentHandlerImpl
        implements FragmentHandler
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(UnsupportedElementFragmentHandler.class);

    /**
     */
    public UnsupportedElementFragmentHandler()
    {
        super();
    }

    /**
     * @throws UnsupportedOperationException
     *      Always thrown, indicating that this element is not supported.
     */
    public void startFragment(
            final DocumentContextTracker context,
            final Attributes attributes
    )
            throws SAXException, UnsupportedOperationException
    {
        throw new UnsupportedOperationException(TraceUtil.formatObj(context.getCurrentPath()));
    }

}