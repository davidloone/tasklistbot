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

import au.id.loone.util.DGLUtil;
import au.id.loone.util.tracing.TraceUtil;

/**
 *  Transport object for storing a single property of a reference item.
 *
 *  @author David G Loone
 */
public abstract class RefListItemPropertyTO
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(RefListItemPropertyTO.class);

    /**
     *  Current value of the <b>name</b> property.
     */
    private String name;

    /**
     */
    public RefListItemPropertyTO()
    {
        super();
    }

    /**
     */
    public boolean equals(
            final Object otherObj
    )
    {
        final boolean result;

        if (otherObj == null) {
            result = false;
        }
        else if (otherObj == this) {
            result = true;
        }
        else if (otherObj instanceof RefListItemPropertyTO) {
            final RefListItemPropertyTO other = (RefListItemPropertyTO)otherObj;
            result = DGLUtil.equals(name, other.name);
        }
        else {
            result = false;
        }

        return result;
    }

    /**
     */
    public int hashCode()
    {
        return DGLUtil.hashCode(name);
    }

    /**
     */
    public String toString()
    {
        return RefListItemPropertyTO.class.getName() + "[" +
                TraceUtil.formatObj(name, "name") + "]";
    }

    /**
     *  Setter method for the <b>name</b> property.
     */
    public void setName(
            final String name
    )
    {
        this.name = name;
    }

    /**
     *  Getter method for the <b>name</b> property.
     */
    public String getName()
    {
        return name;
    }

}
