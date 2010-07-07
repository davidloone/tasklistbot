package au.id.loone.util.reflist;

import au.id.loone.util.tracing.TraceUtil;

/**
 * For converting to/from JavaBean properties.
 *
 * @author David G Loone
 */
public final class BeanPropertyConverter
        implements org.apache.commons.beanutils.Converter
{

    /**
     */
    public BeanPropertyConverter()
    {
        super();
    }

    /**
     */
    @SuppressWarnings({"unchecked"})
    public Object convert(
            final Class type,
            final Object value
    )
    {
        final Object result;

        if (value == null) {
            result = null;
        }
        else {
            result = RefList.factory(type, value.toString());
            if (result == null) {
                throw new IllegalArgumentException("No reference item for code " +
                        TraceUtil.formatObj(value) + " in class " + type.getName());
            }
        }

        return result;
    }

}