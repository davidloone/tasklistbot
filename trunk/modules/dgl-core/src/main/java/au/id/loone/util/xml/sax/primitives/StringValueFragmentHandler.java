package au.id.loone.util.xml.sax.primitives;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.xml.sax.SimpleValueFragmentHandler;

import org.apache.log4j.Logger;

/**
 * Simple content handler for an element containing a xs:string type.
 *
 * @author David G Loone
 */
public class StringValueFragmentHandler
        extends SimpleValueFragmentHandler<String>
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(StringValueFragmentHandler.class);

    /**
     */
    public StringValueFragmentHandler()
    {
        super();
    }

    /**
     */
    public String valueFactory(
            final String content
    )
    {
        return content;
    }

}