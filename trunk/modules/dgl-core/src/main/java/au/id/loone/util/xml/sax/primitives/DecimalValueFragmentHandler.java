package au.id.loone.util.xml.sax.primitives;

import java.math.BigDecimal;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.xml.sax.SimpleValueFragmentHandler;

import org.apache.log4j.Logger;

/**
 * Simple content handler for an element containing a xs:decimal type.
 *
 * @author David G Loone
 */
public class DecimalValueFragmentHandler
        extends SimpleValueFragmentHandler<BigDecimal>
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(DecimalValueFragmentHandler.class);

    /**
     */
    public DecimalValueFragmentHandler()
    {
        super();
    }

    /**
     */
    public BigDecimal valueFactory(
            final String content
    )
            throws NumberFormatException
    {
        final BigDecimal result;

        if (DGLStringUtil.isNullOrEmpty(content)) {
            result = null;
        }
        else {
            result = new BigDecimal(content);
        }

        return result;
    }

}