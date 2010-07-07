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

import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 *  Simple abstract implementation of a chained entity resolver.
 *
 *  @author David G Loone
 */
public abstract class ChainedXMLEntityResolverImpl
        implements ChainedXMLEntityResolver
{

    /**
     *  The current value of the <b>xmlEntityResolver</b> property.
     */
    private XMLEntityResolver itsXmlEntityResolver;

    /**
     *  Make a new chained entity resolver.
     */
    protected ChainedXMLEntityResolverImpl()
    {
        super();

        itsXmlEntityResolver = null;
    }

    /**
     */
    public XMLInputSource resolveEntity(
            final XMLResourceIdentifier resourceIdentifier
    )
            throws XNIException, IOException
    {
        // The value to return.
        XMLInputSource result;

        if (itsXmlEntityResolver == null) {
            result = null;
        }
        else {
            result = itsXmlEntityResolver.resolveEntity(resourceIdentifier);
        }

        return result;
    }

    /**
     */
    public void setXmlEntityResolver(
            XMLEntityResolver xmlEntityResolver
    )
    {
        itsXmlEntityResolver = xmlEntityResolver;
    }

    /**
     */
    public XMLEntityResolver getXmlEntityResolver()
    {
        return itsXmlEntityResolver;
    }

}