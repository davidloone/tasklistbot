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

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import au.id.loone.util.DGLStringUtil;
import au.id.loone.util.tracing.TraceUtil;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 *  An entity resolver that knows about the location of various external entities.
 *  Currently, all mappings are hard coded.
 *
 *  <h3>System Identifier to Package Directory Mappings</h3>
 *
 *  <p>The following external entity prefixes are mapped to package directories:
 *      <ul>
 *          <li>"<code>http://loone.id.au/xml/</code>"
 *              <ul>&rarr; "<code>/au/id/loone/xml/</code>"</ul>
 *          </li>
 *      </ul>
 *  </p>
 *
 *  @author David G Loone
 */
public final class DGLEntityResolver
        extends ChainedEntityResolverImpl
        implements ChainedEntityResolver, EntityResolver
{

    /**
     *  Logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private final static Logger LOG = TraceUtil.getLogger(DGLEntityResolver.class);

    /**
     *  The array of prefixes that should be mapped to package directories.
     *  Elements of this array have a one to one mapping to
     *  elements of the {@link #SYSTEM_ID_TO_PACKAGE_VALUES} array.
     */
    private final static String[] SYSTEM_ID_TO_PACKAGE_KEYS;

    /**
     *  The array of prefix substitutions.
     *  Elements of this array have a one to one mapping to
     *  elements of the {@link #SYSTEM_ID_TO_PACKAGE_KEYS} array.
     */
    private final static String[] SYSTEM_ID_TO_PACKAGE_VALUES;

    /**
     *  Fill in the system id to package map.
     */
    static {
        SYSTEM_ID_TO_PACKAGE_KEYS = new String[] {
            "http://loone.id.au/xml/"
        };
        SYSTEM_ID_TO_PACKAGE_VALUES = new String[] {
            "/au/id/loone/xml/"
        };
    }

    /**
     *  A list of extensions to try if the file cannot be found.
     */
    private static final String[] EXTENSIONS = new String[] {
            ".dtd",
            ".xsd"
    };

    /**
     *  A reference class to use for loading resources.
     */
    private Class itsReferenceClass;

    /**
     *  Make a new entity resolver.
     *  Resources will be loaded by the class loader that loaded this class.
     */
    public DGLEntityResolver()
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
    public DGLEntityResolver(
            final Class referenceClass
    )
    {
        super();

        LOG.trace("DGLEntityResolver(" +
                TraceUtil.formatObj(referenceClass) + ")");

        itsReferenceClass = referenceClass;
    }

    /**
     */
    public InputSource resolveEntity(
            String publicId,
            String systemId
    )
            throws org.xml.sax.SAXException, java.io.IOException
    {
        LOG.trace("resolveEntity(" +
                TraceUtil.formatObj(publicId) + ", " +
                TraceUtil.formatObj(systemId) + ")");

        // The value to return.
        InputSource result = null;

        // Search the system id to package map.
        final InputStream in = makeStream(systemId);
        if (in != null) {
            result = new InputSource(in);
        }

        // No resolution? Then delegate to the superclass.
        if (result == null) {
            result = super.resolveEntity(publicId, systemId);
        }

        LOG.trace("~resolveEntity = " + TraceUtil.formatObj(result));
        return result;
    }

    /**
     */
    private InputStream makeStream(
            final String systemId
    )
    {
        LOG.trace("makeStream(" + TraceUtil.formatObj(systemId) + ")");
        InputStream result;

        if (DGLStringUtil.isNullOrEmpty(systemId)) {
            result = null;
        }
        else {
            result = null;
            for (int i = 0; i < SYSTEM_ID_TO_PACKAGE_KEYS.length; i++) {
                String prefix = SYSTEM_ID_TO_PACKAGE_KEYS[i];
                if (systemId.startsWith(prefix)) {
                    String path = SYSTEM_ID_TO_PACKAGE_VALUES[i] + systemId.substring(prefix.length());
                    LOG.trace("resolveEntity: attempting to resolve URL " +
                            TraceUtil.formatObj(systemId) + " as resource " +
                            TraceUtil.formatObj(path) + " (" + TraceUtil.formatObj(getClass().getResource(path)) + ")");
                    result = itsReferenceClass.getResourceAsStream(path);
                    if (result == null) {
                        for (int j = 0; (j < EXTENSIONS.length) && (result == null); j++) {
                            final String extendedPath = path + EXTENSIONS[j];
                            LOG.trace("resolveEntity: attempting to resolve URL " +
                                    TraceUtil.formatObj(systemId) + " as resource " +
                                    TraceUtil.formatObj(extendedPath) + " (" +
                                    TraceUtil.formatObj(getClass().getResource(extendedPath)) + ")");
                            result = itsReferenceClass.getResourceAsStream(extendedPath);
                        }
                    }
                    if (result != null) {
                        LOG.trace("resolveEntity: successfully resolved URL " +
                                TraceUtil.formatObj(systemId) + " as resource " +
                                TraceUtil.formatObj(path) + " (" +
                                TraceUtil.formatObj(getClass().getResource(path)) + ")");
                        break;
                    }
                }
            }
        }

        if (result == null) {
            try {
                final URL systemIdUrl = new URL(systemId);
                final String resourcePath = DGLXMLEntityResolver.findResourcePath(systemIdUrl);
                result = itsReferenceClass.getResourceAsStream(resourcePath);

            }
            catch (final MalformedURLException e) {
                // Do nothing.
            }
        }

        LOG.trace("~makeStream = " + TraceUtil.formatObj(result));
        return result;
    }

    /**
     *  List the mappings.
     */
    public static void main(
            String[] argv
    )
    {
        System.out.println("System id to package directories:");
        for (int i = 0; i < SYSTEM_ID_TO_PACKAGE_KEYS.length; i++) {
            System.out.println("  " + SYSTEM_ID_TO_PACKAGE_KEYS[i] +
                    " -> " + SYSTEM_ID_TO_PACKAGE_VALUES[i]);
        }
    }

}