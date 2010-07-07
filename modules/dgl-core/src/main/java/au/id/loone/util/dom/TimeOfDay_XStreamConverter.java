/*
 * Copyright 2007, David G Loone
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.id.loone.util.dom;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.log4j.Logger;

/**
 * XStream converter for {@link TimeOfDay}.
 *
 * @author David G Loone
 */
public class TimeOfDay_XStreamConverter
        implements com.thoughtworks.xstream.converters.Converter
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = Logger.getLogger(TimeOfDay_XStreamConverter.class);

    /**
     */
    public TimeOfDay_XStreamConverter()
    {
        super();
    }

    /**
     */
    public boolean canConvert(
            final Class cl
    )
    {
        return TimeOfDay.class.isAssignableFrom(cl);
    }

    /**
     */
    public void marshal(
            final Object valueObj,
            final HierarchicalStreamWriter writer,
            final MarshallingContext context
    )
    {
        final TimeOfDay value = (TimeOfDay)valueObj;
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
        return new TimeOfDay(reader.getValue());
    }

}