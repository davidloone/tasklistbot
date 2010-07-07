package au.id.loone.util.dom;

import au.id.loone.util.DGLStringUtil;

/**
 * Bean converter for {@link Duration}.
 *
 * @author David G Loone
 */
public final class Duration_Converter
        implements org.apache.commons.beanutils.Converter
{

    /**
     */
    public Duration_Converter()
    {
        super();
    }

    /**
     */
    @Override
    public Object convert(
            Class type,
            Object value
    )
    {
        final Duration result;

        if (value == null) {
            result = null;
        }
        else {
            final String valueStr = value.toString();
            result = DGLStringUtil.isNullOrEmpty(valueStr) ? null : new Duration(valueStr);
        }

        return result;
    }

}