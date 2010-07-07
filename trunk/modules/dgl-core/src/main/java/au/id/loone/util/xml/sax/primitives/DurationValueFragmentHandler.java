package au.id.loone.util.xml.sax.primitives;

import java.util.regex.Pattern;

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
public class DurationValueFragmentHandler
        extends SimpleValueFragmentHandler<DGLDateTimeUtil.ISO8601Duration>
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(DurationValueFragmentHandler.class);

    /**
     */
    final Pattern pattern = Pattern.compile("(-?)P((\\d\\d?)Y(\\d\\d?)M(\\d\\d?)D)?T(\\d\\d?)H(\\d\\d?)M(\\d\\d?)S");

    /**
     */
    public DurationValueFragmentHandler()
    {
        super();
    }

    /**
     */
    public DGLDateTimeUtil.ISO8601Duration valueFactory(
            final String content
    )
    {
        final DGLDateTimeUtil.ISO8601Duration result;

        if (DGLStringUtil.isNullOrEmpty(content)) {
            result = null;
        }
        else {
            result = DGLDateTimeUtil.ISO8601Duration.factory(content);
        }

        return result;
    }

}