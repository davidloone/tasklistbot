package au.id.loone.util.dom;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.log4j.Logger;

/**
 * XStream converter for {@link Duration}.
 *
 * @author David G Loone
 */
public class Duration_XStreamConverter
        implements com.thoughtworks.xstream.converters.Converter
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(Duration_XStreamConverter.class);

    /**
     */
    public Duration_XStreamConverter()
    {
        super();
    }

    /**
     */
    public boolean canConvert(
            final Class cl
    )
    {
        return Duration.class.isAssignableFrom(cl);
    }

    /**
     */
    public void marshal(
            final Object valueObj,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context
    )
    {
        final Duration value = (Duration)valueObj;
        writer.setValue(value.formatAsHMSS());
    }

    /**
     */
    @SuppressWarnings({"unchecked"})
    public Object unmarshal(
            final HierarchicalStreamReader reader,
            final UnmarshallingContext context
    )
    {
        return new Duration(reader.getValue());
    }

}