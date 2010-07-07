/*
 * Copyright 2009, David G Loone
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

package au.id.loone.util.beans.converters;

import java.util.TimeZone;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.tracing.TraceUtil;

import org.apache.commons.beanutils.Converter;

/**
 * Property convert to convert string to {@link java.util.TimeZone}.
 *
 * @author David G Loone
 */
public class TimeZoneConverter
        implements Converter
{

    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(TimeZoneConverter.class);

    /**
     */
    public TimeZoneConverter()
    {
        super();
    }

    /**
     */
    public Object convert(
            final Class type,
            final Object value
    )
    {
        return DGLStringUtil.isNullOrEmpty((String)value) ? null : TimeZone.getTimeZone((String)value);
    }
}
