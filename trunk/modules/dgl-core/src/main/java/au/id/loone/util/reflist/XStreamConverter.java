package au.id.loone.util.reflist;

import au.id.loone.util.tracing.TraceUtil;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.log4j.Logger;

/**
 * For converting to/from an XStream.
 *
 * @author David G Loone
 */
public final class XStreamConverter
        implements com.thoughtworks.xstream.converters.Converter
{

    /**
     */
    public XStreamConverter()
    {
        super();
    }

    /**
     */
    public boolean canConvert(
            final Class cl
    )
    {
        return RefItem.class.isAssignableFrom(cl);
    }

    /**
     */
    public void marshal(
            final Object valueObj,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context
    )
    {
        final RefItem value = (RefItem)valueObj;
        writer.setValue(value.getCode());
    }

    /**
     */
    @SuppressWarnings({"unchecked"})
    public Object unmarshal(
            final HierarchicalStreamReader reader,
            final UnmarshallingContext context
    )
    {
        final RefItem result;

        result = RefList.getItemByCode(context.getRequiredType(), reader.getValue());

        return result;
    }

}