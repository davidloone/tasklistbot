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

package au.id.loone.util.reflist.transobj;

import au.id.loone.util.tracing.TraceUtil;

/**
 *  Transport object for storing a single property of a reference item,
 *  where the property is not an indexed property.
 *
 *  @author David G Loone
 */
public class RefListItemNonIndexedPropertyTO
        extends RefListItemPropertyTO
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(RefListItemNonIndexedPropertyTO.class);

    /**
     *  Current value of the <b>value</b> property.
     */
    private String value;

    /**
     */
    public RefListItemNonIndexedPropertyTO()
    {
        super();
    }

    /**
     */
    public String toString()
    {
        return RefListItemNonIndexedPropertyTO.class.getName() + "{" +
                super.toString() + ", " +
                TraceUtil.formatObj(value, "value");
    }

    /**
     *  Setter method for the <b>value</b> property.
     */
    public void setValue(
            final String value
    )
    {
        this.value = value;
    }

    /**
     *  Getter method for the <b>value</b> property.
     */
    public String getValue()
    {
        return value;
    }

}
