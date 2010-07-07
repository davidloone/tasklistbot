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

import java.util.Set;

import au.id.loone.util.tracing.TraceUtil;

/**
 *  Transport object for storing a single reference item.
 *
 *  @author David G Loone
 */
public class RefListItemTO
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(RefListItemTO.class);

    /**
     *  Current value of the <b>aliases</b> property.
     */
    private Set<String> aliases;

    /**
     *  Current value of the <b>code</b> property.
     */
    private String code;

    /**
     *  Current value of the <b>description</b> property.
     */
    private String description;

    /**
     *  Current value of the <b>obsolete</b> property.
     */
    private boolean obsolete;

    /**
     *  Current value of the <b>ord</b> property.
     */
    private int ord;

    /**
     *  Current value of the <b>properties</b> property.
     */
    private Set<RefListItemPropertyTO> properties;

    /**
     */
    public RefListItemTO()
    {
        super();
    }

    /**
     *  Setter method for the <b>aliases</b> property.
     */
    public void setAliases(
            final Set<String> aliases
    )
    {
        this.aliases = aliases;
    }

    /**
     *  Getter method for the <b>aliases</b> property.
     */
    public Set<String> getAliases()
    {
        return this.aliases;
    }

    /**
     *  Setter method for the <b>code</b> property.
     */
    public void setCode(
            final String code
    )
    {
        this.code = code;
    }

    /**
     *  Getter method for the <b>code</b> property.
     */
    public String getCode()
    {
        return this.code;
    }

    /**
     *  Setter method for the <b>description</b> property.
     */
    public void setDescription(
            final String description
    )
    {
        this.description = description;
    }

    /**
     *  Getter method for the <b>description</b> property.
     */
    public String getDescription()
    {
        return this.description;
    }

    /**
     *  Setter method for the <b>obsolete</b> property.
     */
    public void setObsolete(
            final boolean obsolete
    )
    {
        this.obsolete = obsolete;
    }

    /**
     *  Getter method for the <b>obsolete</b> property.
     */
    public boolean getObsolete()
    {
        return this.obsolete;
    }

    /**
     *  Setter method for the <b>ord</b> property.
     */
    public void setOrd(
            final int ord
    )
    {
        this.ord = ord;
    }

    /**
     *  Getter method for the <b>ord</b> property.
     */
    public int getOrd()
    {
        return this.ord;
    }

    /**
     *  Setter method for the <b>properties</b> property.
     */
    public void setProperties(
            final Set<RefListItemPropertyTO> properties
    )
    {
        this.properties = properties;
    }

    /**
     *  Getter method for the <b>properties</b> property.
     */
    public Set<RefListItemPropertyTO> getProperties()
    {
        return this.properties;
    }

}
