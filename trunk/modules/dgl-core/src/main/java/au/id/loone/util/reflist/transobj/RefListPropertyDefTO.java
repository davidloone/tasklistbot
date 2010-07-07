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

import javax.xml.namespace.QName;

import au.id.loone.util.tracing.TraceUtil;

/**
 *  Transport object for storing the definition of a Reference List property.
 *
 *  @author David G Loone
 */
public class RefListPropertyDefTO
{

    /**
     *  Log4j logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final org.apache.log4j.Logger LOG = TraceUtil.getLogger(RefListPropertyDefTO.class);

    /**
     *  Current value of the <b>isIndexed</b> property.
     */
    private boolean isIndexed;

    /**
     *  Current value of the <b>name</b> property.
     */
    private String name;

    /**
     *  Current value of the <b>type</b> property.
     */
    private QName type;

    /**
     */
    public RefListPropertyDefTO()
    {
        super();
    }

    /**
     *  Setter method for the <b>isIndexed</b> property.
     */
    public void setIsIndexed(
            final boolean isIndexed
    )
    {
        this.isIndexed = isIndexed;
    }

    /**
     *  Getter method for the <b>isIndexed</b> property.
     */
    public boolean getIsIndexed()
    {
        return isIndexed;
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
     *  Setter method for the <b>type</b> property.
     */
    public void setType(
            final QName type
    )
    {
        this.type = type;
    }

    /**
     *  Getter method for the <b>type</b> property.
     */
    public QName getType()
    {
        return type;
    }

}
