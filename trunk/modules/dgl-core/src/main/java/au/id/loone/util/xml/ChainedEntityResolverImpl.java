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

package au.id.loone.util.xml;

import java.io.IOException;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *  Simple abstract implementation of a chained entity resolver.
 *
 *  @author David G Loone
 */
public abstract class ChainedEntityResolverImpl
        implements ChainedEntityResolver
{

    /**
     *  The current value of the <b>entityResolver</b> property.
     */
    private EntityResolver itsEntityResolver;

    /**
     *  Make a new chained entity resolver.
     */
    protected ChainedEntityResolverImpl()
    {
        super();

        itsEntityResolver = null;
    }

    /**
     *  Implementing xstreamClasses should delegate here if they cannot resolve the entity.
     *  This method will then consult the next entity resolver in the chain
     *  (<i>ie</i>, the value of the <b>entityResolver</b> property),
     *  if there is one.
     */
    public InputSource resolveEntity(
            String publicId,
            String systemId
    )
            throws SAXException, IOException
    {
        // The value to return.
        InputSource result;

        if (itsEntityResolver == null) {
            result = null;
        }
        else {
            result = itsEntityResolver.resolveEntity(publicId, systemId);
        }

        return result;
    }

    /**
     */
    public void setEntityResolver(
            EntityResolver entityResolver
    )
    {
        itsEntityResolver = entityResolver;
    }

    /**
     */
    public EntityResolver getEntityResolver()
    {
        return itsEntityResolver;
    }

}