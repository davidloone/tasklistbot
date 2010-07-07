package au.id.loone.util.xml.sax;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;

/**
 * Simple content handler for an empty element.
 *
 * @author David G Loone
 */
public class EmptyElementFragmentHandler
        extends SimpleValueFragmentHandler<Boolean>
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(EmptyElementFragmentHandler.class);

    /**
     */
    public EmptyElementFragmentHandler()
    {
        super();
    }

    /**
     */
    public Boolean valueFactory(
            final String content
    )
    {
        return true;
    }

}