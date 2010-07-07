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

package au.id.loone.util.hibernate;

import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;

/**
 *  @author David G Loone
 */

public abstract class HibernateBase
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(HibernateBase.class);

    /**
     * Current value of the <b>id</b> property.
     */
    private Long id;

    /**
     */
    protected HibernateBase()
    {
        super();
    }

    /**
     */
    public String toString()
    {
        return HibernateBase.class.getName() + "[" +
                TraceUtil.formatObj(id, "id") + "]";
    }

    /**
     * Setter method for the <b>id</b> property.
     */
    public void setId(
            final Long id
    )
    {
        this.id = id;
    }

    /**
     * Getter method for the <b>id</b> property.
     */
    public Long getId()
    {
        return id;
    }

}
