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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.tracing.TraceUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

/**
 *  An entity resolver that knows about the location of various external entities.
 *
 *  <h3>System Identifier to Package Directory Mappings</h3>
 *
 *  <p>System identifiers must be valid URIs, and are mapped lexically to a classloader path.
 *      For example, a URI like:
 *      <blockquote>http://c.b.a/d/e.xsd</blockquote>
 *      maps to a classloader path like:
 *      <blockquote>a/b/c/d/e.xsd</blockquote>
 *  </p>
 *
 *  @author David G Loone
 */
public final class DGLXMLEntityResolver
        extends ChainedXMLEntityResolverImpl
        implements ChainedXMLEntityResolver, XMLEntityResolver
{

    /**
     *  Logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private final static Logger LOG = TraceUtil.getLogger(DGLXMLEntityResolver.class);

    /**
     *  A reference class to use for loading resources.
     */
    private Class itsReferenceClass;

    /**
     *  Make a new entity resolver.
     *  Resources will be loaded by the class loader that loaded this class.
     */
    public DGLXMLEntityResolver()
    {
        super();

        itsReferenceClass = DGLEntityResolver.class;
    }

    /**
     *  Make a new entity resolver,
     *  using a reference class for loading resources.
     *
     *  @param referenceClass
     *      A reference class, whose classloader should be used for loading resources.
     */
    public DGLXMLEntityResolver(
            final Class referenceClass
    )
    {
        super();

        itsReferenceClass = referenceClass;
    }

    /**
     */
    public XMLInputSource resolveEntity(
            final XMLResourceIdentifier resourceIdentifier
    )
            throws XNIException, IOException
    {
        LOG.trace("resolveEntity(" + TraceUtil.formatObj(resourceIdentifier) + ")");

        XMLInputSource result = null;

        // Search the system id to package map.
        InputStream in = makeStream(resourceIdentifier.getNamespace());
        if (in == null) {
            in = makeStream(resourceIdentifier.getExpandedSystemId());
        }
        if (in != null) {
            result = new XMLInputSource(resourceIdentifier.getPublicId(),
                    resourceIdentifier.getExpandedSystemId(), resourceIdentifier.getBaseSystemId(),
                    in, null);
        }

        // No resolution? Then delegate to the superclass.
        if (result == null) {
            result = super.resolveEntity(resourceIdentifier);
        }

        LOG.trace("~resolveEntity = " + TraceUtil.formatObj(result));
        return result;
    }

    /**
     */
    private InputStream makeStream(
            final String systemId
    )
            throws MalformedURLException
    {
        InputStream result;

       if (DGLStringUtil.isNullOrEmpty(systemId)) {
            result = null;
        }
        else {
            final URL url = new URL(systemId);
            final String path = findResourcePath(url);
            result = itsReferenceClass.getResourceAsStream(path);
            if (result != null) {
                LOG.trace("resolveEntity: successfully resolved URL " +
                        TraceUtil.formatObj(systemId) + " as resource " +
                        TraceUtil.formatObj(path) + " (" +
                        TraceUtil.formatObj(getClass().getResource(path)) + ")");
            }
        }

        return result;
    }

    /**
     *  Go figure out a Java package name from a URI.
     */
    static String findResourcePath(
            final URL url
    )
    {
        final StringBuffer packageBuilder = new StringBuffer();

        // Reverse the host name.
        {
            final String[] hostNameComponents = StringUtils.split(url.getHost(), '.');
            for (int i = (hostNameComponents.length - 1); i >= 0; i--) {
                packageBuilder.append('/');
                packageBuilder.append(hostNameComponents[i]);
            }
        }

        // Extract the file name.
        packageBuilder.append(url.getFile());

        return packageBuilder.toString();
    }

    /**
     *  List the mappings.
     */
    public static void main(
            String[] argv
    )
    {
        try {
            System.out.println(findResourcePath(new URL("http://loone.id.au/xml/c.xsd")));
        }
        catch (final Exception e) {
            e.printStackTrace(System.err);
        }
    }

}