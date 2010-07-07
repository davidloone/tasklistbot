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

package au.id.loone.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map;

import au.id.loone.util.tracing.TraceUtil;
import au.id.loone.util.xml.DGLXMLUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Represents a configuration resource.
 *      A configuration resource is any classloader accessible resource.
 *      Often, these are {@link Properties properties files},
 *      but they can be any kind of file.
 *
 * <h3>Overriding Properties</h3>
 *
 * <p>Prefixing override properties in this way helps ensure that the override property names
 *      are unique across the application.</p>
 *
 * @author David G Loone
 */
public final class ConfigResource
        implements Comparable
{

    /**
     * Logger for this class.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    private static final Logger LOG = TraceUtil.getLogger(ConfigResource.class);

    /**
     * The set of configuration resources.
     */
    private static final Set<ConfigResource> CONFIG_RESOURCES = new TreeSet<ConfigResource>();

    /**
     * The class used as a classloader context for finding the resource.
     */
    private Class itsRefClass;

    /**
     * The actual resource path.
     */
    private String resourcePath;

    /**
     * Current value of the <b>substitutions</b> property.
     */
    private Map<String, String> substitutions;

    /**
     */
    private ConfigResource(
            final Class refClass,
            final String resourcePath
    )
    {
        super();

        itsRefClass = refClass;
        this.resourcePath = resourcePath;

        synchronized (CONFIG_RESOURCES) {
            CONFIG_RESOURCES.add(this);
        }
    }

    /**
     * Make a new configuration resource object from a path.
     *
     * @param refClass
     *      The class to use as a context for loading the resource.
     * @param path
     *      The path to the resource to load.
     * @return
     *      The newly created configuration resource.
     *      This will never be equal to <code>null</code>
     *      (if it does not exist, then an exception would be thrown instead).
     * @throws ResourceNotFoundException
     *      Thrown if the resource does not exist.
     */
    public static ConfigResource factory(
            final Class refClass,
            final String path
    )
            throws ResourceNotFoundException
    {
        // Need to be careful with use of loggers to avoid circular class loading problems
        // (ie, logger may not yet have been set). If there is no logger, then log to stdout.

        // The value to return.
        final ConfigResource result;
        // The absolute resource path.
        final String absPath;

        if (path.startsWith("/")) {
            // Use the path as-is.
            absPath = path;
        }
        else {
            // Use the given path as a suffix. The prefix is derived from the package name of the supplied class.
            final String[] classComponents = StringUtils.split(refClass.getName(), ".");
            final StringBuffer buf = new StringBuffer();
            buf.append("/");
            for (int i = 0; i < (classComponents.length - 1); i++) {
                buf.append(classComponents[i]);
                buf.append("/");
            }
            buf.append(path);
            absPath = buf.toString();
        }

        // And check that the resource exists.
        try {
            final InputStream is = refClass.getResourceAsStream(absPath);
            if (is == null) {
                throw new ResourceNotFoundException(absPath);
            }
            else {
                // Now that we have satisfied ourselves that the resource exists, can close the unused stream.
                is.close();
            }
        }
        catch (final IOException e) {
            throw new ResourceNotFoundException(absPath, e);
        }
        result = new ConfigResource(refClass, absPath);

        return result;
    }

    /**
     * Make a new configuration resource object corresponding to a class,
     * but with a given extension.
     *
     * <p>The actual resource used has the same name as the class <code>companionClass</code>,
     *      with the extension given by <code>extension</code>
     *      (without the period character).</p>
     *
     * @param refClass
     *      The class to use as a context for loading the resource.
     * @param companionClass
     *      The companion class for the resource.
     * @param extension
     *      The filename extension of the resource.
     * @return
     *      The newly created configuration resource.
     *      This will never be equal to <code>null</code>
     *      (if it does not exist, then an exception would be thrown instead).
     * @throws ResourceNotFoundException
     *      Thrown if the resource does not exist.
     */
    public static ConfigResource factory(
            final Class refClass,
            final Class companionClass,
            final String extension
    )
            throws ResourceNotFoundException
    {
        final String path = "/" + StringUtils.join(StringUtils.split(companionClass.getName(), '.'), '/') +
                "." + extension;
        return factory(refClass, path);
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
        else if (otherObj instanceof ConfigResource) {
            final ConfigResource other = (ConfigResource)otherObj;
            result = DGLUtil.equals(resourcePath, other.resourcePath);
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
        return DGLUtil.hashCode(resourcePath);
    }

    /**
     * @return
     *      The result of comparing the paths.
     */
    public int compareTo(
            final Object otherObj
    )
    {
        return resourcePath.compareTo(((ConfigResource)otherObj).resourcePath);
    }

    /**
     * Get an input stream on the resource.
     *
     * @return
     *      An input stream on the resource.
     *      This will never be equal to <code>null</code>.
     *      This input stream <b>must</b> be closed when it is no longer required.
     */
    public InputStream asInputStream()
    {
        return itsRefClass.getResourceAsStream(resourcePath);
    }

    /**
     * Parse the resource into an XML DOM tree.
     *
     * @param validate
     *      Determines whether the XML reader validates or not.
     *      A value of <code>true</code> validates the input document.
     * @return
     *      The DOM object from the resource.
     *      This will never be equal to <code>null</code>.
     *
     * @see DGLXMLUtils#createDocument
     */
    public Document asXMLDocument(
            final boolean validate
    )
            throws IOException, SAXException
    {
        // The value to return.
        Document result = null;
        // The input stream.
        InputStream is = null;

        //noinspection CaughtExceptionImmediatelyRethrown
        try {
            is = asInputStream();
            result = (is == null) ? null : DGLXMLUtils.createDocument(new InputSource(is), validate);
        }
        catch (final IOException e) {
            // Just rethrow he exception (after doing the finally clause).
            throw e;
        }
        catch (final SAXException e) {
            // Just rethrow he exception (after doing the finally clause).
            throw e;
        }
        finally {
            try {
                // And close the input stream.
                if (is != null) {
                    is.close();
                }
            }
            catch (IOException e) {
                // Can't do much about this.
            }
        }

        return result;
    }

    /**
     * Get the set of all available config resources.
     *
     * @return
     *      An unmodifiable set of all the config resources that have been accessed.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public static Set<ConfigResource> getConfigResources()
    {
        final Set<ConfigResource> result;

        synchronized (CONFIG_RESOURCES) {
            result = Collections.unmodifiableSet(CONFIG_RESOURCES);
        }

        return result;
    }

    /**
     * Getter method for the <b>content</b> property.
     */
    public String getContent()
    {
        final StringWriter writer = new StringWriter();
        InputStream is = null;
        try {
            is = asInputStream();
            final Reader reader = new InputStreamReader(is);
            int ch = reader.read();
            while (ch != -1) {
                writer.write(ch);
                ch = reader.read();
            }
            writer.close();
        }
        catch (final IOException e) {
            throw new ResourceNotFoundException(resourcePath, e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (final IOException e) {
                    // Do nothing.
                }
            }
        }

        return writer.toString();
    }

    /**
     * Getter method for the <b>properties</b> property.
     */
    public Properties getProperties()
    {
        // The value to return.
        final Properties result;

        // Load the resource into a properties object.
        InputStream is = null;
        try {
            is = asInputStream();
            result = new Properties();
            result.load(is);
        }
        catch (final IOException e) {
            throw new ResourceNotFoundException(resourcePath, e);
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (final IOException e) {
                    // Do nothing.
                }
            }
        }

        if (substitutions != null) {
            final StrSubstitutor subs = new StrSubstitutor(substitutions);
            for (final Map.Entry entry : result.entrySet()) {
                result.setProperty((String)entry.getKey(), subs.replace((String)entry.getValue()));
            }
        }

        return result;
    }

    /**
     * Getter method for the <b>resourcePath</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public String getResourcePath()
    {
        return resourcePath;
    }

    /**
     * Setter for the <b>substitutions</b> property.
     */
    public void setSubstitutions(
        final Map<String, String> substitutions
    )
    {
        this.substitutions = substitutions;
    }

    /**
     * Getter method for the <b>substitutions</b> property.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public Map<String, String> getSubstitutions()
    {
        return substitutions;
    }

    /**
     * Getter method for the <b>url</b> property.
     */
    public URL getUrl()
    {
        return itsRefClass.getResource(resourcePath);
    }

}