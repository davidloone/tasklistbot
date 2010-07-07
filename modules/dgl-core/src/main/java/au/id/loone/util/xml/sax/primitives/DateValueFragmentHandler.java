package au.id.loone.util.xml.sax.primitives;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import au.id.loone.util.DGLDateTimeUtil;
import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.xml.sax.SimpleValueFragmentHandler;

import org.apache.log4j.Logger;

/**
 * Simple content handler for an element containing a xs:decimal type.
 *
 * @author David G Loone
 */
@SuppressWarnings({"UnusedDeclaration"})
public class DateValueFragmentHandler
        extends SimpleValueFragmentHandler<Date>
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(DateValueFragmentHandler.class);

    /**
     */
    public DateValueFragmentHandler()
    {
        super();
    }

    /**
     */
    public Date valueFactory(
            final String content
    )
    {
        final Date result;

        if (DGLStringUtil.isNullOrEmpty(content)) {
            result = null;
        }
        else {
            try {
                final Calendar c = DGLDateTimeUtil.parseAsISO8601(content);
                result = (c == null) ? null : c.getTime();
            }
            catch (final ParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        return result;
    }

}