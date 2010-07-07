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

import au.id.loone.util.DGLStringUtil;

/**
 * Bean converter for {@link TimeOfDay}.
 *
 * @author David G Loone
 */
public class TimeOfDay_Converter
        implements org.apache.commons.beanutils.Converter
{

    /**
     */
    public TimeOfDay_Converter()
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
        final TimeOfDay result;

        if (value == null) {
            result = null;
        }
        else {
            final String valueStr = value.toString();
            result = DGLStringUtil.isNullOrEmpty(valueStr) ? null : new TimeOfDay(valueStr);
        }

        return result;
    }

}