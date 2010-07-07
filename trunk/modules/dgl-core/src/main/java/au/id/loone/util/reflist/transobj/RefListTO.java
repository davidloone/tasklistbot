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

import java.util.List;

import au.id.loone.util.tracing.TraceUtil;

/**
 *  Transport object for storing the contents of a reference list.
 *
 *  @author Davd G Loone
 */
public class RefListTO
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(RefListTO.class);

    /**
     *  Current value of the <b>description</b> property.
     */
    private String description;

    /**
     *  Current value of the <b>implClass</b> property.
     */
    private Class implClass;

    /**
     *  Current value of the <b>implClassName</b> property.
     */
    private String implClassName;

    /**
     *  Current value of the <b>items</b> property.
     */
    private List<RefListItemTO> items;

    /**
     *  Current value of the <b>propertyDefs</b> property.
     */
    private List<RefListPropertyDefTO> propertyDefs;

    /**
     *  Current value of the <b>name</b> property.
     */
    private String name;

    /**
     *  Current value of the <b>version</b> property.
     */
    private String version;

    /**
     */
    public RefListTO()
    {
        super();
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
        return description;
    }

    /**
     *  Setter method for the <b>implClass</b> property.
     */
    public void setImplClass(
            final Class implClass
    )
    {
        this.implClass = implClass;
    }

    /**
     *  Getter method for the <b>implClass</b> property.
     */
    public Class getImplClass()
    {
        return implClass;
    }

    /**
     *  Setter method for the <b>implClassName</b> property.
     */
    public void setImplClassName(
            final String implClassName
    )
    {
        this.implClassName = implClassName;
    }

    /**
     *  Getter method for the <b>implClassName</b> property.
     */
    public String getImplClassName()
    {
        return implClassName;
    }

    /**
     *  Setter method for the <b>items</b> property.
     */
    public void setItems(
            final List<RefListItemTO> items
    )
    {
        this.items = items;
    }

    /**
     *  Getter method for the <b>items</b> property.
     */
    public List<RefListItemTO> getItems()
    {
        return items;
    }

    /**
     *  Setter method for the <b>propertyDefs</b> property.
     */
    public void setPropertyDefs(
            final List<RefListPropertyDefTO> propertyDefs
    )
    {
        this.propertyDefs = propertyDefs;
    }

    /**
     *  Getter method for the <b>propertyDefs</b> property.
     */
    public List<RefListPropertyDefTO> getPropertyDefs()
    {
        return propertyDefs;
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

    /**
     *  Setter method for the <b>version</b> property.
     */
    public void setVersion(
            final String version
    )
    {
        this.version = version;
    }

    /**
     *  Getter method for the <b>version</b> property.
     */
    public String getVersion()
    {
        return version;
    }

}
