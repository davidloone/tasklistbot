package au.id.loone.util.xml.sax.primitives;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.dom.TimeOfDay;
import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.xml.sax.SimpleValueFragmentHandler;

import org.apache.log4j.Logger;

/**
 * Simple content handler for an element containing a xs:time type.
 *
 * @author David G Loone
 */
public class TimeOfDayValueFragmentHandler
        extends SimpleValueFragmentHandler<TimeOfDay>
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(TimeOfDayValueFragmentHandler.class);

    /**
     */
    public TimeOfDayValueFragmentHandler()
    {
        super();
    }

    /**
     */
    public TimeOfDay valueFactory(
            final String content
    )
    {
        final TimeOfDay result;

        if (DGLStringUtil.isNullOrEmpty(content)) {
            result = null;
        }
        else {
            result = new TimeOfDay(content);
        }

        return result;
    }

}